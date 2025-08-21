package com.example.api.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddressRequest {

	@Getter
	@NoArgsConstructor
	public static class Create {

		@NotBlank(message = "주소를 입력해주세요.")
		private String address;

		@NotBlank(message = "상세 주소를 입력해주세요.")
		private String addressDetail;

		@NotBlank(message = "연락처를 입력해주세요.")
		@Pattern(regexp = "^0\\d{1,2}-\\d{3,4}-\\d{4}$", message = "핸드폰 번호 양식을 지켜주세요.")
		private String phone;

		@NotBlank(message = "받는 사람을 입력해주세요.")
		private String recipientName;

	}

}
