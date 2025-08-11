package com.example.api.controller.product;

import com.example.api.dto.product.ProductResponse;
import com.example.api.usecase.product.GetProductDetailUseCase;
import com.example.api.usecase.product.ListProductsUseCase;
import com.example.common.response.ApiResponse;
import com.example.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

	private final ListProductsUseCase listProductsUseCase;
	private final GetProductDetailUseCase getProductDetailUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<ProductResponse.GetAll>>> getAll(
		@PageableDefault() Pageable pageable
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.success(listProductsUseCase.getAll(pageable)));
	}

	@GetMapping("{productId}")
	public ResponseEntity<ApiResponse<ProductResponse.Get>> getProduct(
		@PathVariable String productId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.success(getProductDetailUseCase.getProduct(productId)));
	}

}
