package com.example.api.facade.product;

import com.example.api.dto.product.ProductResponse;
import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.common.response.PageResponse;
import com.example.domain.entity.Product;
import com.example.domain.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductFacade {

	private final ProductRepository productRepository;

	public PageResponse<ProductResponse> getAll(Pageable pageable) {

		Page<Product> page = productRepository.findAll(pageable);

		List<ProductResponse> list = page.stream()
			.map(p -> new ProductResponse(
				p.getId(),
				p.getCategory().getName(),
				p.getName(),
				p.getDescription(),
				p.getPrice().toString(),
				p.getStock()
			))
			.collect(Collectors.toList());

		return PageResponse.of(list, pageable.getPageNumber(), page.getSize(),
			page.getTotalElements());
	}

	public ProductResponse getProduct(String productId) {

		Product product = productRepository.findById(Long.parseLong(productId))
			.orElseThrow(() -> new ApiException("존재하지 않는 상품 번호 입니다.", ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST));

		return new ProductResponse(
			product.getId(),
			product.getCategory().getName(),
			product.getName(),
			product.getDescription(),
			product.getPrice().toString(),
			product.getStock()
		);
	}

}
