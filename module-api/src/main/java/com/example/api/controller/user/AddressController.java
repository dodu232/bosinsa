package com.example.api.controller.user;

import com.example.api.dto.user.AddressRequest;
import com.example.api.infra.auth.CustomUserDetails;
import com.example.api.usecase.user.CreateAddressUseCase;
import com.example.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me/addresses")
@RequiredArgsConstructor
public class AddressController {

	private final CreateAddressUseCase createAddressUseCase;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createAddress(
		@AuthenticationPrincipal CustomUserDetails user,
		@Valid @RequestBody AddressRequest.Create request
	) {
		createAddressUseCase.createAddress(user.getId(), request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
	}

}
