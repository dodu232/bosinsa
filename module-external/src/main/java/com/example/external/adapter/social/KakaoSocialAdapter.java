package com.example.external.adapter.social;

import com.example.external.dto.KakaoUserInfoResponse;
import com.example.external.social.SocialUserInfo;
import com.example.external.social.kakao.KakaoApiClient;
import com.example.external.social.kakao.KakaoAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("kakao")
@RequiredArgsConstructor
public class KakaoSocialAdapter implements SocialLoginAdapter {

	private final KakaoAuthClient authClient;
	private final KakaoApiClient apiClient;

	private String contentType = "application/x-www-form-urlencoded;charset=utf-8";
	private String grantType = "authorization_code";

	@Value("${kakao.client-id}")
	private String clientId;

	@Value("${kakao.redirect-uri}")
	private String redirectUri;

	@Value("${kakao.client-secret}")
	private String clientSecret;

	private String getToken(String code) {
		return authClient.getToken(
			"Content-Type: application/x-www-form-urlencoded;charset=utf-8",
			"authorization_code",
			clientId,
			redirectUri,
			code,
			clientSecret
		).getAccessToken();
	}

	@Override
	public SocialUserInfo login(String code) {
		String token = "Bearer " + getToken(code);

		KakaoUserInfoResponse info = apiClient.getUserInfo(token, contentType);

		return new SocialUserInfo(info.kakaoAccount.email);
	}


}
