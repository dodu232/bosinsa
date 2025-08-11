package com.example.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCache {

	private Long id;
	private String category;
	private String name;
	private String description;
	private String price;

//	public static ProductCache from(ProductResponse r) {
//		return new ProductCache(r.getId(), r.getCategory(), r.getName(), r.getDescription(),
//			r.getPrice());
//	}
}
