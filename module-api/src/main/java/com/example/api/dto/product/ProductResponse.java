package com.example.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ProductResponse {

	@Getter
	@AllArgsConstructor
	public static class Get {

		private Long id;
		private String category;
		private String name;
		private String price;
		private String description;
		private int stock;
	}

	@Getter
	@AllArgsConstructor
	public static class GetAll {

		private Long id;
		private String category;
		private String name;
		private String price;
	}

}
