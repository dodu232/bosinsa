package com.example.api.usecase.auth;

import com.example.api.dto.auth.SignupRequest;
import org.springframework.stereotype.Service;

@Service
public interface SignupUseCase {

	void signUp(SignupRequest dto);
}
