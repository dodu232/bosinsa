package com.example.api.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreatedPayload {

	private Long orderId;
	private Long userId;
	private BigDecimal amount;
	private List<Item> items;
	private LocalDateTime occurredAt;

	@Getter
	public static class Item {

		private Long productId;
		private int quantity;
		private BigDecimal price;

		public Item(Long productId, int quantity, BigDecimal price) {
			this.productId = productId;
			this.quantity = quantity;
			this.price = price;
		}
	}

}
