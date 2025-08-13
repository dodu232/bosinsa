package com.example.api.infra.cache;

import java.util.Map;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

public class VariableTtlRedisCacheManager extends RedisCacheManager {

	private final RedisCacheWriter writer;
	private final TtlPolicy ttlPolicy;

	public VariableTtlRedisCacheManager(
		RedisCacheWriter writer,
		RedisCacheConfiguration defaultCacheConfiguration,
		Map<String, RedisCacheConfiguration> initialCacheConfigurations,
		TtlPolicy ttlPolicy
	) {
		super(writer, defaultCacheConfiguration, initialCacheConfigurations);
		this.writer = writer;
		this.ttlPolicy = ttlPolicy;
	}

	@Override
	protected RedisCache createRedisCache(String name,
		@Nullable RedisCacheConfiguration cacheConfig) {
		RedisCacheConfiguration cfg =
			(cacheConfig != null) ? cacheConfig : RedisCacheConfiguration.defaultCacheConfig();
		return new VariableTtlRedisCache(name, writer, cfg, ttlPolicy);
	}
}
