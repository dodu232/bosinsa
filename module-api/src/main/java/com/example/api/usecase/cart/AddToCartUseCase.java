package com.example.api.usecase.cart;

import com.example.api.dto.cart.CartRequest;
import com.example.api.dto.cart.CartView;
import org.springframework.stereotype.Service;

@Service
public interface AddToCartUseCase {

	CartView addItem(String cartId, CartRequest.AddItems dto);
}
