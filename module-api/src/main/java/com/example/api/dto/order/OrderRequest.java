package com.example.api.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderRequest {

	@Getter
	@NoArgsConstructor
	public static class Create {

		@NotNull(message = "주소 ID는 필수입니다.")
		private Long addressId;

		@NotEmpty(message = "구매 항목은 최소 1개 이상이어야 합니다.")
		@Valid
		private List<OrderRequest.Item> items;
	}

	@Getter
	@NoArgsConstructor
	public static class Item {

		@NotNull(message = "상품 ID는 필수입니다.")
		private Long productId;

		@Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
		private int quantity;
	}

}
