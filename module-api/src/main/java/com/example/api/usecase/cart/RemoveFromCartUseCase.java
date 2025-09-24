package com.example.api.usecase.cart;

import com.example.api.dto.cart.CartRequest;
import com.example.api.dto.cart.CartView;
import org.springframework.stereotype.Service;

@Service
public interface RemoveFromCartUseCase {

	CartView removeItem(String cartId, CartRequest.DeleteItems dto);
}
