package com.example.api.usecase.auth;

import com.example.api.dto.auth.SigninRequest;

public interface SigninUseCase {

	String signIn(SigninRequest dto);
}
