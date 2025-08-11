package com.example.api.infra.cache;

import com.example.api.dto.product.ProductResponse;
import com.example.api.dto.product.ProductResponse.GetAll;
import com.example.common.response.PageResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductPageCache {

	private final StringRedisTemplate redis;
	private final ObjectMapper objectMapper;

	private String versionKey(String scope) {
		return "products:ver:" + scope;
	}

	private String hashParams(Map<String, String> params) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			StringBuilder sb = new StringBuilder();
			params.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.forEach(e -> sb.append(e.getKey()).append("=").append(e.getValue()).append("&"));
			byte[] digest = md.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
			StringBuilder hex = new StringBuilder();
			for (byte b : digest) {
				hex.append(String.format("%02x", b));
			}
			return hex.toString();
		} catch (Exception e) {
			return Integer.toHexString(params.hashCode());
		}
	}

	private String pageKey(String scope, String hash, int page, int size, long ver) {
		return String.format("products:page:%s:%d:%d:%d", hash, page, size, ver);
	}

	public long getVersion(String scope) {
		String v = redis.opsForValue().get(versionKey(scope));
		return (v == null) ? 0 : Long.parseLong(v);
	}

	public void bumpVersion(String scope) {
		redis.opsForValue().increment(versionKey(scope));
	}

	public PageResponse<ProductResponse.GetAll> get(String scope, Map<String, String> params,
		int page, int size) {
		long ver = getVersion(scope);
		String key = pageKey(scope, hashParams(params), page, size, ver);
		String json = redis.opsForValue().get(key);
		if (json == null) {
			return null;
		}
		try {
			return objectMapper.readValue(json, new TypeReference<PageResponse<GetAll>>() {
			});
		} catch (Exception e) {
			return null;
		}
	}

	public void put(String scope, Map<String, String> params, int page, int size,
		PageResponse<ProductResponse.GetAll> response) {
		long ver = getVersion(scope);
		String key = pageKey(scope, hashParams(params), page, size, ver);
		Duration ttl = (page == 0) ? Duration.ofMinutes(10) : Duration.ofSeconds(60);
		try {
			String json = objectMapper.writeValueAsString(response);
			redis.opsForValue().set(key, json, ttl);
		} catch (Exception e) {
		}
	}
}
