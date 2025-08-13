package com.example.api.config;

import com.example.api.infra.cache.PageTtlPolicy;
import com.example.api.infra.cache.TtlPolicy;
import com.example.api.infra.cache.VariableTtlRedisCacheManager;
import com.example.common.response.PageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Map;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory();
	}

	@Bean
	public TtlPolicy ttlPolicy() {
		return new PageTtlPolicy();
	}

	@Bean
	public VariableTtlRedisCacheManager redisCacheManager(RedisConnectionFactory cf,
		ObjectMapper om, TtlPolicy ttlPolicy) {
		RedisCacheWriter writer = RedisCacheWriter.nonLockingRedisCacheWriter(cf);

		Jackson2JsonRedisSerializer<PageResponse> pageRespSer = new Jackson2JsonRedisSerializer<>(
			om, PageResponse.class);

		RedisCacheConfiguration base = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new StringRedisSerializer())).serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(pageRespSer))
			.entryTtl(Duration.ofMinutes(1));

		Map<String, RedisCacheConfiguration> initial = Map.of("productPages", base);

		return new VariableTtlRedisCacheManager(writer, base, initial, ttlPolicy);
	}


}
