package com.example.api.usecase.auth;

import com.example.api.dto.auth.SigninRequest;
import com.example.api.facade.auth.AuthFacade;
import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.common.util.JwtUtil;
import com.example.domain.entity.User;
import com.example.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

		if(!userDomainService.isPasswordMatch(dto.getPassword(), user.getPassword())) {
			throw new ApiException("비밀번호 불일치", ErrorType.INVALID_PARAMETER, HttpStatus.BAD_REQUEST);
		}

		return jwtUtil.generateToken(dto.getEmail());
	}
}
