package com.example.api.facade.auth;

import com.example.external.social.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialAuthFacade {

	private final OAuthService oAuthService;

	public void login(String provider, String code) {

		String userEmail = oAuthService.socialLogin(provider, code).getEmail();
		System.out.println(userEmail);
	}
}
