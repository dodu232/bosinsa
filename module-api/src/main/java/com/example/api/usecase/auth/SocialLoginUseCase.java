package com.example.api.usecase.auth;

public interface SocialLoginUseCase {

	String socialLogin(String provider, String code);
}
