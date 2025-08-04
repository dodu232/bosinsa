package com.example.external.social;

import com.example.external.adapter.social.SocialLoginAdapter;

public class SocialLoginContext {

	public SocialUserInfo login(SocialLoginAdapter socialLoginAdapter, String code) {
		return socialLoginAdapter.login(code);
	}
}
