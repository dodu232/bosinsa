package com.example.api.usecase.auth;

import com.example.api.dto.auth.SigninRequest;
import org.springframework.stereotype.Service;

@Service
public interface SigninUseCase {

	String signIn(SigninRequest dto);
}
