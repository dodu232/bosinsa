package com.example.api.usecase.auth;

import com.example.api.facade.auth.AuthFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupUseCase {

    private final AuthFacade authFacade;

    public void signUp() {
        System.out.println("Sign up use case executed");
        authFacade.signUp();
    }

}
