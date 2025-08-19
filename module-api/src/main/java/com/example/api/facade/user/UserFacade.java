package com.example.api.facade.user;

import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.domain.entity.User;
import com.example.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

	private final UserRepository userRepository;

	public void isEmailDuplicated(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new ApiException("중복된 이메일", ErrorType.INVALID_PARAMETER, HttpStatus.BAD_REQUEST);
		}
	}

	public void save(User user) {
		userRepository.save(user);
	}

	public User getByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new ApiException("존재하지 않는 이메일", ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST));
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public User getById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new ApiException("존재하지 않는 회원번호", ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST));
	}

}
