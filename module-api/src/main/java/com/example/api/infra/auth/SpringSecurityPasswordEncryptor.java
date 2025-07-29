package com.example.api.infra.auth;

import com.example.domain.support.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringSecurityPasswordEncryptor implements PasswordEncryptor {
	private final PasswordEncoder passwordEncoder;

	@Override
	public String encode(String password) {
		return passwordEncoder.encode(password);
	}
}
