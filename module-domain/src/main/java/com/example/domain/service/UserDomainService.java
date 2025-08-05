package com.example.domain.service;

import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.domain.support.PasswordEncryptor;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDomainService {

	private final PasswordEncryptor passwordEncryptor;

	public String encrypt(String password) {
		return passwordEncryptor.encode(password);
	}

	public void isPasswordMatch(String password, String encryptedPassword) {
		if (!passwordEncryptor.matches(password, encryptedPassword)) {
			throw new ApiException("비밀번호 불일치", ErrorType.INVALID_PARAMETER);
		}
	}

	public String generatedDummyPw() {
		return UUID.randomUUID().toString();
	}
}
