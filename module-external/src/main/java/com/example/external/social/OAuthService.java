package com.example.external.social;

import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.external.adapter.social.KakaoSocialAdapter;
import com.example.external.adapter.social.SocialLoginAdapter;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

	private final Map<String, SocialLoginAdapter> socialLoginStrategyMap;

	public OAuthService(
		KakaoSocialAdapter kakaoSocialAdapter
	) {
		this.socialLoginStrategyMap = Map.of(
			"kakao", kakaoSocialAdapter
		);
	}

	public SocialUserInfo socialLogin(String provider, String code) {
		SocialLoginAdapter adapter = socialLoginStrategyMap.get(provider);
		if (adapter == null) {
			throw new ApiException("지원하지 않는 소셜 로그인 플랫폼입니다.", ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST);
		}
		return new SocialLoginContext().login(adapter, code);
	}

}
