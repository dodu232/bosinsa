package com.example.api.usecase.product;

import com.example.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListProductsUseCase {

	public PageResponse<?> getAll(Pageable pageable){




		return PageResponse.of()
	}
}
