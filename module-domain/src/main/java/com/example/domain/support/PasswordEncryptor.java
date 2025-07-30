package com.example.domain.support;

public interface PasswordEncryptor {
	String encode(String password);
	boolean matches(String password, String encodedPassword);
}
