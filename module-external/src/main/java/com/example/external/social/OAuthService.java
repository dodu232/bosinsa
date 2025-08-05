package com.example.external.social;

import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

	private final Map<String, SocialLoginService> socialLoginStrategyMap;

	public OAuthService(
		KakaoSocialService kakaoSocialService
	) {
		this.socialLoginStrategyMap = Map.of(
			"kakao", kakaoSocialService
		);
	}

	public SocialUserInfo socialLogin(String provider, String code) {
		SocialLoginService service = socialLoginStrategyMap.get(provider);
		if (service == null) {
			throw new ApiException("지원하지 않는 소셜 로그인 플랫폼입니다.", ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST);
		}
		return new SocialLoginContext().login(service, code);
	}

}
