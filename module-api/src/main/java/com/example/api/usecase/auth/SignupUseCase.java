package com.example.api.usecase.auth;

import com.example.api.dto.auth.SignupRequest;

public interface SignupUseCase {

	void signUp(SignupRequest dto);
}
