package com.example.api.usecase.auth;

import com.example.api.dto.auth.SignupRequest;
import com.example.api.facade.auth.AuthFacade;
import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.domain.entity.User;
import com.example.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupUseCase {

    private final AuthFacade authFacade;
    private final UserDomainService userDomainService;

    public void signUp(SignupRequest request) {
        // email 중복 검사
        if(authFacade.isEmailDuplicated(request.getEmail())) {
            throw new ApiException("중복된 이메일", ErrorType.INVALID_PARAMETER, HttpStatus.BAD_REQUEST);
        }

        String encrypted = userDomainService.encrypt(request.getPassword());
        User user = User.of(request.getEmail(), encrypted, request.getNickname());

        authFacade.save(user);
    }

}
