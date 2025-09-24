package com.example.api.usecase.product;

import com.example.api.dto.product.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public interface GetProductDetailUseCase {

	ProductResponse.Get getProduct(String productId);
}
