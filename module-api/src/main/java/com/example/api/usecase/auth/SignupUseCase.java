package com.example.api.usecase.auth;

import com.example.api.dto.auth.SignupRequest;
import com.example.api.facade.auth.AuthFacade;
import com.example.domain.entity.User;
import com.example.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupUseCase {

	private final AuthFacade authFacade;
	private final UserDomainService userDomainService;

	@Transactional
	public void signUp(SignupRequest dto) {
		authFacade.isEmailDuplicated(dto.getEmail());

		String encrypted = userDomainService.encrypt(dto.getPassword());
		User user = User.of(dto.getEmail(), encrypted, dto.getNickname());

		authFacade.save(user);
	}

}
