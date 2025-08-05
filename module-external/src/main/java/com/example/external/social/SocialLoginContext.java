package com.example.external.social;

public class SocialLoginContext {

	public SocialUserInfo login(SocialLoginService socialLoginService, String code) {
		return socialLoginService.login(code);
	}
}
