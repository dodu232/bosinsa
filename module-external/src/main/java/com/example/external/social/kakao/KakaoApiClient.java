package com.example.external.social.kakao;

import com.example.external.dto.KakaoUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
	name = "kakaoApiClient",
	url = "${kakao.user-info-uri}")
public interface KakaoApiClient {

	@PostMapping
	KakaoUserInfoResponse getUserInfo(
		@RequestHeader("Authorization") String token,
		@RequestHeader("Content-Type") String contentType
	);

}
