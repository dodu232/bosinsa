package com.example.api.facade.auth;

import com.example.api.facade.user.UserFacade;
import com.example.domain.entity.User;
import com.example.domain.enums.LoginProvider;
import com.example.domain.service.UserDomainService;
import com.example.external.social.OAuthService;
import com.example.external.social.SocialUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialAuthFacade {

	private final OAuthService oAuthService;
	private final UserFacade userFacade;
	private final UserDomainService userDomainService;

	public SocialUserInfo fetchSocialUser(String provider, String code) {
		return oAuthService.socialLogin(provider, code);
	}

	public Long getOrCreateSocialUser(SocialUserInfo info, String provider) {
		String email = info.getEmail();
		if (userFacade.existsByEmail(email)) {
			return userFacade.findByEmail(email).getId();
		}

		String dummyPw = userDomainService.generatedDummyPw();
		User user = User.of(info.getEmail(), dummyPw, info.getNickname(),
			LoginProvider.from(provider));

		userFacade.save(user);

		return user.getId();
	}
}
