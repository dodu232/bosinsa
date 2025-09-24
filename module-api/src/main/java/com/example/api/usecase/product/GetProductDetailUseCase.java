package com.example.api.usecase.product;

import com.example.api.dto.product.ProductResponse;

public interface GetProductDetailUseCase {

	ProductResponse.Get getProduct(String productId);
}
