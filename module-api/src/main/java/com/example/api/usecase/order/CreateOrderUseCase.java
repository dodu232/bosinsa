package com.example.api.usecase.order;

import com.example.api.facade.order.OrderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

	private final OrderFacade orderFacade;

	/**
	 * 장바구니 확인 후, 주문 생성 요청
	 * 결제 시도
	 *
	 */


}
