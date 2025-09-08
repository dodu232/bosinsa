package com.example.contracts.payload;

import com.example.contracts.EventPayload;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEventPayload implements EventPayload {

	private Long orderId;
	private Long userId;
	private Long addressId;
	private BigDecimal amount;
	private String status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
