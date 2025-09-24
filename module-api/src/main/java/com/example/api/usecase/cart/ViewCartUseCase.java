package com.example.api.usecase.cart;

import com.example.api.dto.cart.CartView;
import org.springframework.stereotype.Service;

@Service
public interface ViewCartUseCase {

	CartView getCart(String cartId);
}
