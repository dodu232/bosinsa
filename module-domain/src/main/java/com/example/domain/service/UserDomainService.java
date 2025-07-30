package com.example.domain.service;

import com.example.domain.support.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDomainService {

	private final PasswordEncryptor passwordEncryptor;

	public String encrypt(String password) {
		return passwordEncryptor.encode(password);
	}

	public boolean isPasswordMatch(String password, String encryptedPassword) {
		return passwordEncryptor.matches(password, encryptedPassword);
	}
}
