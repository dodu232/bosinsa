package com.example.api.usecase.product;

import com.example.api.dto.product.ProductResponse;
import com.example.api.facade.product.ProductFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductDetailUseCase {

	private final ProductFacade productFacade;

	public ProductResponse getProduct(String productId) {

		return productFacade.getProduct(productId);
	}
}
