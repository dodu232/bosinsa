package com.example.api.usecase.auth;

import com.example.api.config.JwtUtil;
import com.example.api.dto.auth.SigninRequest;
import com.example.api.facade.user.UserFacade;
import com.example.domain.entity.User;
import com.example.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SigninUseCase {

	private final UserFacade userFacade;
	private final JwtUtil jwtUtil;
	private final UserDomainService userDomainService;

	@Transactional(readOnly = true)
	public String signIn(SigninRequest dto) {
		User user = userFacade.findByEmail(dto.getEmail());

		userDomainService.isPasswordMatch(dto.getPassword(), user.getPassword());

		return jwtUtil.generateToken(dto.getEmail());
	}
}
