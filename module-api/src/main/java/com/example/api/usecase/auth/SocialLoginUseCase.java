package com.example.api.usecase.auth;

import com.example.api.facade.auth.SocialAuthFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLoginUseCase {

	private final SocialAuthFacade socialLoginFacade;

	public void socialLogin(String provider, String code) {
		socialLoginFacade.login(provider, code);
	}
}
