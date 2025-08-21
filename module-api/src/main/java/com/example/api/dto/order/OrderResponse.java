package com.example.api.dto.order;

import com.example.domain.enums.OrderStatus;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class OrderResponse {

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Create {

		private Long id;
		private String address;
		private String addressDetail;
		private Long userId;
		private BigDecimal amount;
		private OrderStatus status;
	}

}
