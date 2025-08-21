package com.example.api.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreatedPayload {

	public Long orderId;
	public Long userId;
	public BigDecimal amount;
	public List<Item> items;
	public LocalDateTime occurredAt;

	@Getter
	public static class Item {

		public Long productId;
		public int quantity;
		public BigDecimal price; // 단가 or 라인합, 팀 컨벤션에 맞춰 명확히

		public Item(Long productId, int quantity, BigDecimal price) {
			this.productId = productId;
			this.quantity = quantity;
			this.price = price;
		}
	}

}
