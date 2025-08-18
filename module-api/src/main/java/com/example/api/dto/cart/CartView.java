package com.example.api.dto.cart;

import java.time.LocalDateTime;
import java.util.List;

public record CartView(
	String cartId,
	List<Item> items,
	int totalQuantity,
	String totalPrice,
	LocalDateTime updatedAt
) {

	public record Item(Long productId, int quantity, String price) {

	}
}