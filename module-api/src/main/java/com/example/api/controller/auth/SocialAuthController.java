package com.example.api.controller.auth;

import com.example.api.usecase.auth.SocialLoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/social")
@RequiredArgsConstructor
public class SocialAuthController {

	private final SocialLoginUseCase socialLoginUseCase;

	@GetMapping("/{provider}")
	public ResponseEntity<?> redirect(@PathVariable String provider,
		@RequestParam("code") String code) {

		socialLoginUseCase.socialLogin(provider, code);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
