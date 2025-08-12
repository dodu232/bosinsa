package com.example.domain.repository;

import com.example.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {

	Page<Product> findByCategory(Pageable pageable, String category);
}
