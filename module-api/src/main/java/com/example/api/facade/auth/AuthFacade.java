package com.example.api.facade.auth;

import com.example.api.dto.auth.SignupRequest;
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
public class AuthFacade implements SignupUseCase {

	private final UserRepository userRepository;
	private final UserDomainService userDomainService;

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
