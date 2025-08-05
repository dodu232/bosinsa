package com.example.api.usecase.auth;

import com.example.api.dto.auth.SignupRequest;
import com.example.api.facade.user.UserFacade;
import com.example.domain.entity.User;
import com.example.domain.enums.LoginProvider;
import com.example.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupUseCase {

	private final UserFacade userFacade;
	private final UserDomainService userDomainService;

	@Transactional
	public void signUp(SignupRequest dto) {
		userFacade.isEmailDuplicated(dto.getEmail());

		String encrypted = userDomainService.encrypt(dto.getPassword());
		User user = User.of(dto.getEmail(), encrypted, dto.getNickname(), LoginProvider.LOCAL);

		userFacade.save(user);
	}

}
