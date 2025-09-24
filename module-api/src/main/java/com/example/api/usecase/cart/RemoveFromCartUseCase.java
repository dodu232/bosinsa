package com.example.api.usecase.cart;

import com.example.api.dto.cart.CartRequest;
import com.example.api.dto.cart.CartView;

public interface RemoveFromCartUseCase {

	CartView removeItem(String cartId, CartRequest.DeleteItems dto);
}
