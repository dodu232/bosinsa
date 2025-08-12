package com.example.api.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductRequest {

	@Getter
	@NoArgsConstructor
	public static class Page {

		private String category;

	}


}
