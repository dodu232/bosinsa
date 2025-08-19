package com.example.api.facade.order;

import com.example.domain.repository.AddressRepository;
import com.example.domain.repository.OrderItemRepository;
import com.example.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final AddressRepository addressRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;

}
