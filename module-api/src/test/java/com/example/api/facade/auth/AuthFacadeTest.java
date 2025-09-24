package com.example.api.facade.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.example.api.config.JwtUtil;
import com.example.api.dto.auth.SigninRequest;
import com.example.api.dto.auth.SignupRequest;
import com.example.common.exception.ApiException;
import com.example.domain.entity.User;
import com.example.domain.enums.LoginProvider;
import com.example.domain.repository.UserRepository;
import com.example.domain.service.UserDomainService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class AuthFacadeTest {

	private final UserRepository userRepository = mock(UserRepository.class);
	private final UserDomainService userDomainService = mock(UserDomainService.class);
	private final JwtUtil jwtUtil = mock(JwtUtil.class);

	private final AuthFacade authFacade = new AuthFacade(userRepository, userDomainService,
		jwtUtil);

	@Nested
	@DisplayName("signIn")
	class SignInTest {

		@Test
		@DisplayName("이메일이 존재하고 비밀번호가 일치하면 토큰을 발급한다")
		void signIn_success() {
			// given
			SigninRequest dto = new SigninRequest("test@example.com", "rawPassword");
			User user = User.of("test@example.com", "encodedPassword", "tester",
				LoginProvider.LOCAL);
			ReflectionTestUtils.setField(user, "id", 1L);

			given(userRepository.findByEmail(dto.getEmail())).willReturn(Optional.of(user));
			willDoNothing().given(userDomainService)
				.isPasswordMatch(dto.getPassword(), user.getPassword());
			given(jwtUtil.generateToken(1L, user.getEmail(), user.getNickname())).willReturn(
				"jwt-token");

			// when
			String token = authFacade.signIn(dto);

			// then
			assertThat(token).isEqualTo("jwt-token");
			then(userRepository).should().findByEmail(dto.getEmail());
			then(userDomainService).should().isPasswordMatch(dto.getPassword(), user.getPassword());
			then(jwtUtil).should().generateToken(1L, user.getEmail(), user.getNickname());
		}

		@Test
		@DisplayName("존재하지 않는 이메일이면 예외가 발생한다")
		void signIn_invalidEmail() {
			// given
			SigninRequest dto = new SigninRequest("notfound@example.com", "pw");
			given(userRepository.findByEmail(dto.getEmail())).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> authFacade.signIn(dto))
				.isInstanceOf(ApiException.class)
				.hasMessageContaining("존재하지 않는 이메일");
		}
	}

	@Nested
	@DisplayName("signUp")
	class SignUpTest {

		@Test
		@DisplayName("중복되지 않은 이메일이면 회원가입에 성공한다")
		void signUp_success() {
			// given
			SignupRequest dto = new SignupRequest("new@example.com", "pw123", "nick");
			given(userRepository.existsByEmail(dto.getEmail())).willReturn(false);
			given(userDomainService.encrypt(dto.getPassword())).willReturn("encryptedPw");
			User newUser = User.of(dto.getEmail(), "encryptedPw", dto.getNickname(),
				LoginProvider.LOCAL);
			given(userRepository.save(any(User.class))).willReturn(newUser);

			// when
			authFacade.signUp(dto);

			// then
			then(userRepository).should().existsByEmail(dto.getEmail());
			then(userDomainService).should().encrypt(dto.getPassword());
			then(userRepository).should().save(any(User.class));
		}

		@Test
		@DisplayName("이메일이 이미 존재하면 예외가 발생한다")
		void signUp_duplicateEmail() {
			// given
			SignupRequest dto = new SignupRequest("dup@example.com", "pw", "nick");
			given(userRepository.existsByEmail(dto.getEmail())).willReturn(true);

			// when & then
			assertThatThrownBy(() -> authFacade.signUp(dto))
				.isInstanceOf(ApiException.class)
				.hasMessageContaining("중복된 이메일");
		}
	}

}