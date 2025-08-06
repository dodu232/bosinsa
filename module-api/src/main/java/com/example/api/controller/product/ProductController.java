package com.example.api.controller.product;

import com.example.api.usecase.product.ListProductsUseCase;
import com.example.common.response.ApiResponse;
import com.example.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

	private final ListProductsUseCase listProductsUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<?>>> getAll(
		@PageableDefault() Pageable pageable
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.success(listProductsUseCase.getAll(pageable)));
	}

}
