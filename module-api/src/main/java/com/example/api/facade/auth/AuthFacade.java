package com.example.api.facade.auth;

import com.example.api.config.JwtUtil;
import com.example.api.dto.auth.SigninRequest;
import com.example.api.dto.auth.SignupRequest;
import com.example.api.usecase.auth.SigninUseCase;
import com.example.api.usecase.auth.SignupUseCase;
import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.domain.entity.User;
import com.example.domain.enums.LoginProvider;
import com.example.domain.repository.UserRepository;
import com.example.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthFacade implements SignupUseCase, SigninUseCase {

	private final UserRepository userRepository;
	private final UserDomainService userDomainService;
	private final JwtUtil jwtUtil;

	@Override
	public String signIn(SigninRequest dto) {
		User user = userRepository.findByEmail(dto.getEmail())
			.orElseThrow(() -> new ApiException("존재하지 않는 이메일입니다. email= " + dto.getEmail(),
				ErrorType.INVALID_PARAMETER, HttpStatus.BAD_REQUEST));

		userDomainService.isPasswordMatch(dto.getPassword(), user.getPassword());

		return jwtUtil.generateToken(user.getId(), user.getEmail(), user.getNickname());
	}

	@Override
	@Transactional
	public void signUp(SignupRequest dto) {
		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new ApiException("중복된 이메일", ErrorType.INVALID_PARAMETER, HttpStatus.BAD_REQUEST);
		}

		String encrypted = userDomainService.encrypt(dto.getPassword());
		User user = User.of(dto.getEmail(), encrypted, dto.getNickname(), LoginProvider.LOCAL);

		userRepository.save(user);
	}
}
