package com.example.api.facade.cart;

import com.example.api.dto.cart.CartRequest;
import com.example.api.dto.cart.CartView;
import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.domain.entity.Product;
import com.example.domain.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartFacade {

	private final ProductRepository productRepository;

	private CartFacade self;

	private final String cacheName = "carts";
	private final String keyGenerator = "cartKeyGenerator";

	@Autowired
	public void setSelf(@Lazy CartFacade self) {
		this.self = self;
	}

	@Cacheable(cacheNames = cacheName, keyGenerator = keyGenerator)
	public CartView getCart(String cartId) {
		return new CartView(cartId, List.of(), 0, "0", LocalDateTime.now());
	}

	@CachePut(cacheNames = cacheName, keyGenerator = keyGenerator)
	public CartView addItem(String cartId, List<CartRequest.Item> reqItems) {
		CartView current = self.getCart(cartId);

		Map<Long, Integer> delta = new HashMap<>();
		for (CartRequest.Item it : reqItems) {
			if (it.getQuantity() <= 0) {
				continue;
			}
			delta.merge(it.getProductId(), it.getQuantity(), Integer::sum);
		}
		if (delta.isEmpty()) {
			return current;
		}

		List<CartView.Item> merged = new ArrayList<>(current.items().size() + delta.size());
		Set<Long> handled = new HashSet<>();

		for (CartView.Item it : current.items()) {
			Long pid = it.productId();
			Integer addQty = delta.get(pid);
			if (addQty != null) {
				int newQty = it.quantity() + addQty;
				if (newQty > 0) {
					merged.add(new CartView.Item(pid, newQty, it.price()));
				}
				handled.add(pid);
			} else {
				merged.add(it);
			}
		}

		for (Map.Entry<Long, Integer> e : delta.entrySet()) {
			Long pid = e.getKey();
			if (handled.contains(pid)) {
				continue;
			}

			int qty = e.getValue();
			if (qty <= 0) {
				continue;
			}

			String price = String.valueOf(getPrice(pid));
			merged.add(new CartView.Item(pid, qty, price));
		}

		return recalc(current.cartId(), merged);
	}

	@CachePut(cacheNames = "carts", keyGenerator = "cartKeyGenerator")
	public CartView removeItem(String cartId, List<Long> productIds) {
		CartView current = self.getCart(cartId);

		Set<Long> removeSet = new HashSet<>(productIds);

		List<CartView.Item> items = current.items().stream()
			.filter(i -> !removeSet.contains(i.productId()))
			.toList();

		return recalc(cartId, items);
	}

	@CacheEvict(cacheNames = "carts", keyGenerator = "cartKeyGenerator")
	public CartView clear(String cartId) {
		return new CartView(cartId, List.of(), 0, "0", LocalDateTime.now());
	}

	private int getPrice(Long productId) {

		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new ApiException("존재하지 않는 상품 번호 입니다.", ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST));

		return product.getPrice().intValue();
	}

	private static CartView recalc(String cartId, List<CartView.Item> items) {
		int totalQty = 0;
		BigDecimal totalPrice = BigDecimal.ZERO;

		for (CartView.Item item : items) {
			totalQty += item.quantity();

			try {
				BigDecimal price = new BigDecimal(item.price());
				totalPrice = totalPrice.add(price.multiply(BigDecimal.valueOf(item.quantity())));
			} catch (NumberFormatException e) {
				// 가격이 잘못 들어온 경우는 0 처리
			}
		}

		return new CartView(
			cartId,
			items,
			totalQty,
			totalPrice.toPlainString(),
			LocalDateTime.now()
		);
	}
}
