package com.example.api.facade.order;

import com.example.api.dto.order.OrderRequest;
import com.example.api.dto.order.OrderRequest.Item;
import com.example.api.dto.order.OrderResponse;
import com.example.api.dto.order.PaymentComplete;
import com.example.api.dto.order.PaymentInitResponse;
import com.example.api.usecase.order.CompletePaymentUseCase;
import com.example.api.usecase.order.CreateOrderUseCase;
import com.example.api.usecase.order.GetOrderUseCase;
import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.contracts.common.EventType;
import com.example.contracts.payload.OrderCreatedEventPayload;
import com.example.domain.entity.Address;
import com.example.domain.entity.Order;
import com.example.domain.entity.OrderItem;
import com.example.domain.entity.Product;
import com.example.domain.entity.User;
import com.example.domain.enums.OrderStatus;
import com.example.domain.repository.AddressRepository;
import com.example.domain.repository.OrderRepository;
import com.example.domain.repository.ProductRepository;
import com.example.domain.repository.UserRepository;
import com.example.messaging.outbox.OutboxEventPublisher;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderFacade implements CreateOrderUseCase, GetOrderUseCase, CompletePaymentUseCase {

	private final AddressRepository addressRepository;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final OutboxEventPublisher outboxEventPublisher;

	/*
	 * 유저/주소 확인
	 * 같은 productId 병합
	 * 상품 일괄 조회
	 * 총액 계산
	 * 재고 차감
	 * 주문 저장
	 */

	@Override
	@Transactional
	public OrderResponse.Create createOrder(Long userId, OrderRequest.Create dto) {

		User user = userRepository.findById(userId).orElseThrow(
			() -> new ApiException("존재하지 않는 유저입니다.", ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST));

		Address address = addressRepository.findById(dto.getAddressId()).orElseThrow(
			() -> new ApiException("존재하지 않는 주소입니다.", ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST));

		if (!address.getUser().getId().equals(user.getId())) {
			throw new ApiException("해당 회원의 주소지가 아닙니다.", ErrorType.INVALID_PARAMETER,
				HttpStatus.FORBIDDEN);
		}

		Map<Long, Integer> qtyMap = new HashMap<>();
		List<Item> items = dto.getItems();
		for (Item item : items) {
			qtyMap.merge(item.getProductId(), item.getQuantity(), Integer::sum);
		}

		List<Long> productIds = qtyMap.keySet().stream().sorted().toList();
		Map<Long, Product> products = productRepository.findAllById(productIds).stream()
			.collect(Collectors.toMap(Product::getId, p -> p));

		if (products.size() != productIds.size()) {
			Long missing = productIds.stream().filter(id -> !products.containsKey(id)).findFirst()
				.orElse(null);
			throw new ApiException("존재하지 않는 상품입니다. id= " + missing, ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST);
		}

		BigDecimal totalPrice = BigDecimal.ZERO;
		List<OrderItem> orderItems = new ArrayList<>();
		for (Long p : productIds) {
			Product product = products.get(p);
			int quantity = qtyMap.get(p);
			BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf(quantity));
			totalPrice = totalPrice.add(price);

			orderItems.add(OrderItem.of(null, product, quantity, product.getPrice()));
		}

		for (Long p : productIds) {
			int updated = productRepository.decreaseStockIfEnough(p, qtyMap.get(p));
			if (updated == 0) {
				throw new ApiException("재고가 부족합니다. productId=" + p, ErrorType.INVALID_PARAMETER,
					HttpStatus.BAD_REQUEST);
			}
		}

		Order order = Order.of(address, user, totalPrice, OrderStatus.CREATED);
		orderItems.forEach(order::addItem);
		Order saved = orderRepository.save(order);

		return OrderResponse.Create.builder().id(saved.getId())
			.address(saved.getAddress().getAddress())
			.addressDetail(saved.getAddress().getAddressDetail()).userId(saved.getUser().getId())
			.amount(saved.getAmount()).status(saved.getStatus()).build();
	}

	/*
		프론트로 order 정보 전달 (toss 결제용)
	 */

	@Override
	public PaymentInitResponse getOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new ApiException("존재하지 않는 order입니다. orderId=" + orderId,
				ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST));

		if (order.getStatus() == OrderStatus.CANCELLED) {
			throw new ApiException("취소된 주문입니다. orderId = " + orderId, ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST);
		}

		if (order.getStatus() != OrderStatus.CREATED) {
			throw new ApiException("이미 결제된 주문입니다. orderId = " + orderId,
				ErrorType.INVALID_PARAMETER, HttpStatus.BAD_REQUEST);
		}

		String orderName = order.getItems().get(0).getProduct().getName();
		int size = order.getItems().size() - 1;

		String email = order.getUser().getEmail();

		return PaymentInitResponse.builder()
			.orderId(orderId.toString())
			.orderName(orderName + " 외 " + size + " 건")
			.amount(order.getAmount().intValue())
			.customerEmail(email)
			.build();
	}

	/*
	결제 완료 후에 outbox 만들어서 kafka로 전송
	 */

	@Override
	@Transactional
	public void completePayment(PaymentComplete dto) {
		Long orderId = Long.parseLong(dto.getOrderId().substring(8));

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new ApiException("존재하지 않는 order입니다. orderId=" + orderId,
				ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST));

		order.updateStatus(OrderStatus.PAID);

		outboxEventPublisher.publish(
			EventType.ORDER_CREATED,
			OrderCreatedEventPayload.builder()
				.orderId(order.getId())
				.userId(order.getUser().getId())
				.addressId(order.getAddress().getId())
				.amount(order.getAmount())
				.status(order.getStatus().toString())
				.createdAt(order.getCreatedAt())
				.updatedAt(order.getUpdatedAt())
				.build(),
			order.getUserId()
		);

	}
}
