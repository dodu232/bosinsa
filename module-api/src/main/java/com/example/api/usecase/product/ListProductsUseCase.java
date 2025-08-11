package com.example.api.usecase.product;

import com.example.api.dto.product.ProductResponse;
import com.example.api.facade.product.ProductFacade;
import com.example.common.response.PageResponse;
import java.util.HashMap;
import java.util.Map;
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

	public PageResponse<ProductResponse.GetAll> getAllRedis(Pageable pageable, String categoryId,
		String keyword) {
		Map<String, String> params = new HashMap<>();
		if (categoryId != null) {
			params.put("categoryId", categoryId);
		}
		if (keyword != null && !keyword.isBlank()) {
			params.put("keyword", keyword);
		}
		params.put("sort", pageable.getSort().toString());

		return productFacade.getProducts(pageable, params);
	}
}
