package com.example.api.facade.auth;

import com.example.domain.entity.User;
import com.example.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final UserRepository userRepository;

    public void signUp() {
        User user = User.builder()
            .email("test@gmail.com")
            .build();

        userRepository.save(user);
    }
}
