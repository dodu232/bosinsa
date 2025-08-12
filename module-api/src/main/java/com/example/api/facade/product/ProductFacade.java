package com.example.api.facade.product;

import com.example.api.dto.product.ProductResponse;
import com.example.api.infra.cache.ProductPageCache;
import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.common.response.PageResponse;
import com.example.domain.entity.Product;
import com.example.domain.repository.ProductRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private final ProductPageCache cache;

	public PageResponse<ProductResponse.GetAll> getAll(Pageable pageable) {

		Page<Product> page = productRepository.findAll(pageable);

		List<ProductResponse.GetAll> list = page.stream()
			.map(p -> new ProductResponse.GetAll(
				p.getId(),
				p.getCategory().getName(),
				p.getName(),
				p.getPrice().toString()
			))
			.collect(Collectors.toList());

		return PageResponse.of(list, pageable.getPageNumber(), page.getSize(),
			page.getTotalElements());
	}

	public ProductResponse.Get getProduct(String productId) {

		Product product = productRepository.findById(Long.parseLong(productId))
			.orElseThrow(() -> new ApiException("존재하지 않는 상품 번호 입니다.", ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST));

		return new ProductResponse.Get(
			product.getId(),
			product.getCategory().getName(),
			product.getName(),
			product.getPrice().toString(),
			product.getDescription(),
			product.getStock()
		);
	}

	public PageResponse<ProductResponse.GetAll> getProducts(Pageable pageable, String category) {
		String scope;

		if (category == null || category.isBlank() || "all".equals(category)) {
			scope = "all";
		} else {
			scope = "cat:" + category;
		}

		Map<String, String> key = buildCacheKey(scope, pageable);

		// 캐시 조회
		PageResponse<ProductResponse.GetAll> cached =
			cache.get(scope, key, pageable.getPageNumber(), pageable.getPageSize());
		if (cached != null) {
			return cached;
		}

		// 없으면 DB 조회
		Page<Product> page = productRepository.findByCategory(pageable, category);

		List<ProductResponse.GetAll> list = page.stream()
			.map(p -> new ProductResponse.GetAll(
				p.getId(),
				p.getCategory().getName(),
				p.getName(),
				p.getPrice().toString()
			))
			.toList();

		PageResponse<ProductResponse.GetAll> resp =
			PageResponse.of(list, pageable.getPageNumber(), page.getSize(),
				page.getTotalElements());

		cache.put(scope, key, pageable.getPageNumber(), pageable.getPageSize(),
			resp);

		return resp;
	}

	private Map<String, String> buildCacheKey(String scope, Pageable pageable) {
		Map<String, String> key = new HashMap<>();
		key.put("scope", scope);
		key.put("sort", pageable.getSort().toString());
		key.put("page", String.valueOf(pageable.getPageNumber()));
		key.put("size", String.valueOf(pageable.getPageSize()));
		return key;
	}
}
