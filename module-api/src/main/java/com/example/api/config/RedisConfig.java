package com.example.api.config;

import com.example.common.response.PageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
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
	public RedisCacheManager redisCacheManager(RedisConnectionFactory cf, ObjectMapper om) {
		Jackson2JsonRedisSerializer<PageResponse> pageRespSer =
			new Jackson2JsonRedisSerializer<>(om, PageResponse.class);

		RedisCacheConfiguration base = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new GenericJackson2JsonRedisSerializer(om)
			));

		RedisCacheConfiguration productPagesCfg = base.serializeValuesWith(
			RedisSerializationContext.SerializationPair.fromSerializer(pageRespSer)
		);

		return RedisCacheManager.builder(cf)
			.cacheDefaults(base)
			.withInitialCacheConfigurations(Map.of("productPages", productPagesCfg))
			.build();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(
		RedisConnectionFactory cf,
		ObjectMapper objectMapper
	) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(cf);

		StringRedisSerializer keySer = new StringRedisSerializer();
		GenericJackson2JsonRedisSerializer valSer = new GenericJackson2JsonRedisSerializer(
			objectMapper);

		template.setKeySerializer(keySer);
		template.setHashKeySerializer(keySer);
		template.setValueSerializer(valSer);
		template.setHashValueSerializer(valSer);

		template.afterPropertiesSet();
		return template;
	}

}
