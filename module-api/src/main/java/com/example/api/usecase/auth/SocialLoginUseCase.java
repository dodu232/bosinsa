package com.example.api.usecase.auth;

import com.example.api.facade.auth.SocialAuthFacade;
import com.example.common.util.JwtUtil;
import com.example.external.social.SocialUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialLoginUseCase {

	private final SocialAuthFacade socialLoginFacade;
	private final JwtUtil jwtUtil;

	@Transactional
	public String socialLogin(String provider, String code) {

		SocialUserInfo info = socialLoginFacade.fetchSocialUser(provider, code);

		Long userId = socialLoginFacade.getOrCreateSocialUser(info, provider);

		return jwtUtil.generateToken(userId.toString());
	}
}
