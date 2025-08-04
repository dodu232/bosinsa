package com.example.external.social.kakao;

import com.example.external.dto.KakaoTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
	name = "kakaoAuthClient",
	url = "${kakao.token-uri}"
)
public interface KakaoAuthClient {

	@PostMapping
	KakaoTokenResponse getToken(
		@RequestHeader("Content-Type") String contentType,
		@RequestParam("grant_type") String grantType,
		@RequestParam("client_id") String clientId,
		@RequestParam("redirect_uri") String redirectUri,
		@RequestParam("code") String code,
		@RequestParam("client_secret") String secret
	);

}
