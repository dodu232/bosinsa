package com.example.api.dto.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CartRequest {

	@Getter
	@NoArgsConstructor
	public static class AddItems {

		@NotEmpty(message = "장바구니 항목은 최소 1개 이상이어야 합니다.")
		@Valid
		private List<Item> items;
	}

	@Getter
	@NoArgsConstructor
	public static class Item {

		@NotNull(message = "상품 ID는 필수입니다.")
		private Long productId;

		@Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
		private int quantity;
	}

	@Getter
	@NoArgsConstructor
	public static class DeleteItems {

		@NotEmpty
		private List<Long> productIds;
	}

}
