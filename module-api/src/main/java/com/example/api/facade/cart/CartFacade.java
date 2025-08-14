package com.example.api.facade.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartFacade {

	@CachePut(cacheNames = "carts", keyGenerator = "cartKeyGenerator")
	public void addToCart() {

	}
}
