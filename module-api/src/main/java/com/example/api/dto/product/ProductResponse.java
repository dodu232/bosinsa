package com.example.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {

	private Long id;
	private String category;
	private String name;
	private String description;
	private String price;
	private int stock;

}
