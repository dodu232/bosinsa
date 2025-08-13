package com.example.api.infra.cache;

import java.time.Duration;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

public class VariableTtlRedisCache extends RedisCache {

	private final TtlPolicy ttlPolicy;
	private final RedisCacheWriter writer;
	private final RedisCacheConfiguration config;

	public VariableTtlRedisCache(
		String name,
		RedisCacheWriter writer,
		RedisCacheConfiguration config,
		TtlPolicy ttlPolicy
	) {
		super(name, writer, config);
		this.writer = writer;
		this.config = config;
		this.ttlPolicy = ttlPolicy;
	}

	@Override
	public void put(Object key, @Nullable Object value) {
		Object cacheValue = preProcessCacheValue(value);
		if (!isAllowNullValues() && cacheValue == null) {
			evict(key);
			return;
		}
		byte[] cacheKey = serializeCacheKey(createCacheKey(key));
		byte[] cacheVal = serializeCacheValue(cacheValue);
		Duration ttl = resolveTtl(key, value);
		writer.put(getName(), cacheKey, cacheVal, ttl);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
		Object cacheValue = preProcessCacheValue(value);
		if (!isAllowNullValues() && cacheValue == null) {
			return get(key);
		}
		byte[] cacheKey = serializeCacheKey(createCacheKey(key));
		byte[] cacheVal = serializeCacheValue(cacheValue);
		Duration ttl = resolveTtl(key, value);
		byte[] result = writer.putIfAbsent(getName(), cacheKey, cacheVal, ttl);
		if (result == null) {
			return null;
		}
		return new SimpleValueWrapper(fromStoreValue(deserializeCacheValue(result)));
	}

	private Duration resolveTtl(Object key, Object value) {
		Duration d = ttlPolicy.resolveTtl(getName(), key, value);
		return (d != null ? d : config.getTtl());
	}
}