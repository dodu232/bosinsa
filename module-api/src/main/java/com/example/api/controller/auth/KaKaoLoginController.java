package com.example.api.controller.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth/kakao")
public class KaKaoLoginController {

	@Value("${kakao.client-id}")
	private String clientId;

	@Value("${kakao.redirect-uri}")
	private String redirectUri;

	@GetMapping("/page")
	public String socialLogin(Model model) {
		String location =
			"https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&redirect_uri="
				+ redirectUri + "&response_type=code";
		model.addAttribute("location", location);

		return "kakao";
	}
}
