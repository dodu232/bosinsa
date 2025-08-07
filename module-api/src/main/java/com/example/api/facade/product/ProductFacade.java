package com.example.api.facade.product;

import com.example.api.dto.product.ProductResponse;
import com.example.common.response.PageResponse;
import com.example.domain.entity.Product;
import com.example.domain.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductFacade {

	private final ProductRepository productRepository;

	public PageResponse<ProductResponse> getAll(Pageable pageable) {

		Page<Product> page = productRepository.findAll(pageable);

		List<ProductResponse> list = new ArrayList<>();

		for (Product product : page.getContent()) {
			list.add(new ProductResponse(
				product.getId(),
				product.getCategory().getName(),
				product.getName(),
				product.getDescription(),
				product.getStock()
			));
		}

		return PageResponse.of(list, pageable.getPageNumber(), page.getSize(),
			page.getTotalElements());
	}

}
