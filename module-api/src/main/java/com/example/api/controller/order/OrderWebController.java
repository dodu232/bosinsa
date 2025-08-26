package com.example.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/web/orders")
@RequiredArgsConstructor
public class OrderWebController {

	@GetMapping("/toss")
	public String checkoutPage(Model model) {

		return "toss";
	}

	@GetMapping("/success")
	public String successPage(
		Model model,
		@RequestParam String orderId,
		@RequestParam String paymentKey,
		@RequestParam int amount
	) {
		return "success";
	}

	@GetMapping("/fail")
	public String failPage(

	) {
		return "fail";
	}
}
