package com.example.api.usecase.auth;

import com.example.api.dto.auth.SigninRequest;
import com.example.api.facade.auth.AuthFacade;
import com.example.common.util.JwtUtil;
import com.example.domain.entity.User;
import com.example.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SigninUseCase {

	private final AuthFacade authFacade;
	private final JwtUtil jwtUtil;
	private final UserDomainService userDomainService;

	@Transactional(readOnly = true)
	public String signIn(SigninRequest dto) {
		User user = authFacade.findByEmail(dto.getEmail());

		userDomainService.isPasswordMatch(dto.getPassword(), user.getPassword());

		return jwtUtil.generateToken(dto.getEmail());
	}
}
