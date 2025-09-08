package com.example.api.usecase.order;

import com.example.api.dto.order.PaymentComplete;

public interface CompletePaymentUseCase {

	void completePayment(PaymentComplete dto);
}
