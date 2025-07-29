package com.example.api.controller.auth;

import com.example.api.usecase.auth.SignupUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignupUseCase signupUseCase;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(){
        signupUseCase.signUp();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
