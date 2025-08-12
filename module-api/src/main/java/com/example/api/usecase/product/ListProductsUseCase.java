package com.example.api.usecase.product;

import com.example.api.dto.product.ProductResponse;
import com.example.api.facade.product.ProductFacade;
import com.example.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListProductsUseCase {

	private final ProductFacade productFacade;

	public PageResponse<ProductResponse.GetAll> getAll(Pageable pageable) {

		return productFacade.getAll(pageable);
	}

	public PageResponse<ProductResponse.GetAll> getAllRedis(Pageable pageable, String category) {
		return productFacade.getProducts(pageable, category);
	}
}
