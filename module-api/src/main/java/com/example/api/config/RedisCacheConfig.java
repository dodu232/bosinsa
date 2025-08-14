package com.example.api.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

@Configuration
public class RedisCacheConfig {

	@Bean("productPageKeyGenerator")
	public KeyGenerator keyGenerator() {
		return (target, method, params) -> {
			Pageable pageable = (Pageable) params[0];
			String category = (String) params[1];

			String scope = (category == null || category.isBlank() || "all".equals(category))
				? "all" : "cat:" + category;

			return String.join("|",
				"scope=" + scope,
				"page=" + pageable.getPageNumber(),
				"size=" + pageable.getPageSize(),
				"sort=" + pageable.getSort().toString()
			);
		};
	}

	@Bean("cartKeyGenerator")
	public KeyGenerator cartKeyGenerator() {
		return (target, method, params) -> {
			String cartId = (String) params[0];
			return "cart:" + cartId;
		};
	}
}
