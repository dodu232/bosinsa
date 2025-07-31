package com.example.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigninRequest {

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Size(min = 4, max = 12)
	private String password;
}
