package com.example.api.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentComplete {

	String orderId;
	String paymentKey;
	int amount;

}
