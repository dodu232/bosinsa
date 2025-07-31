package com.example.api.controller.auth;

import com.example.api.dto.auth.SigninRequest;
import com.example.api.dto.auth.SignupRequest;
import com.example.api.usecase.auth.SigninUseCase;
import com.example.api.usecase.auth.SignupUseCase;
import com.example.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignupUseCase signupUseCase;
    private final SigninUseCase signinUseCase;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(
        @Valid @RequestBody SignupRequest request
    ){
        signupUseCase.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<Void>> signIn(
        @Valid @RequestBody SigninRequest request
    ){
        String token = signinUseCase.signIn(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .header("Authorization", "Bearer " + token)
            .body(ApiResponse.success(null));
    }
}
