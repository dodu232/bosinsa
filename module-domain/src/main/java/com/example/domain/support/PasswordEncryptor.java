package com.example.domain.support;

import org.springframework.stereotype.Component;

@Component
public interface PasswordEncryptor {
	String encode(String password);
}
