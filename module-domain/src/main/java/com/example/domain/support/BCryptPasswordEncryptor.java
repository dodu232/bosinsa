package com.example.domain.support;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncryptor implements PasswordEncryptor {

	private final BCryptPasswordEncoder delegate = new BCryptPasswordEncoder();

	@Override
	public String encode(String password) {
		return delegate.encode(password);
	}

	@Override
	public boolean matches(String password, String encodedPassword) {
		return delegate.matches(password, encodedPassword);
	}
}