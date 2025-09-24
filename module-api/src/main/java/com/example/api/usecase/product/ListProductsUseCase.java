package com.example.api.usecase.product;

import com.example.api.dto.product.ProductResponse;
import com.example.common.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ListProductsUseCase {

	PageResponse<ProductResponse.GetAll> getAllProducts(Pageable pageable);

	PageResponse<ProductResponse.GetAll> getAllRedis(Pageable pageable, String category);
}
