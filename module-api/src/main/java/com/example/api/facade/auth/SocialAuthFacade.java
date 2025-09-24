package com.example.api.facade.auth;

import com.example.api.config.JwtUtil;
import com.example.api.usecase.auth.SocialLoginUseCase;
import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.domain.entity.User;
import com.example.domain.enums.LoginProvider;
import com.example.domain.repository.UserRepository;
import com.example.domain.service.UserDomainService;
import com.example.external.social.OAuthService;
import com.example.external.social.SocialUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialAuthFacade implements SocialLoginUseCase {

	private final OAuthService oAuthService;
	private final UserRepository userRepository;
	private final UserDomainService userDomainService;
	private final JwtUtil jwtUtil;

	@Override
	@Transactional
	public String socialLogin(String provider, String code) {

		SocialUserInfo info = oAuthService.socialLogin(provider, code);

		Long userId = getOrCreateSocialUser(info, provider);

		return jwtUtil.generateToken(userId, info.getEmail(), info.getEmail());
	}

	private Long getOrCreateSocialUser(SocialUserInfo info, String provider) {
		final String email = info.getEmail();

		return userRepository.findByEmail(email)
			.map(User::getId)
			.orElseGet(() -> {
				String dummyPw = userDomainService.generatedDummyPw();

				User user = User.of(email, dummyPw, info.getNickname(),
					LoginProvider.from(provider));

				try {
					User saved = userRepository.save(user);

					return saved.getId();
				} catch (DataIntegrityViolationException e) {
					return userRepository.findByEmail(email)
						.map(User::getId)
						.orElseThrow(
							() -> new ApiException("존재하지 않는 이메일입니다. email= " + info.getEmail(),
								ErrorType.INVALID_PARAMETER, HttpStatus.BAD_REQUEST));
				}
			});
	}
}
