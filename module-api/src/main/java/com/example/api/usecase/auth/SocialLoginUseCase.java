package com.example.api.usecase.auth;

import org.springframework.stereotype.Service;

@Service
public interface SocialLoginUseCase {

	String socialLogin(String provider, String code);
}
