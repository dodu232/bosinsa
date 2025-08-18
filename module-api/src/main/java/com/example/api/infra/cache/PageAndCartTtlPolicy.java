package com.example.api.infra.cache;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class PageAndCartTtlPolicy implements TtlPolicy {


	@Override
	public Duration resolveTtl(String cacheName, Object key, Object value) {
		String k = String.valueOf(key);

		if ("productPages".equals(cacheName)) {
			if (key instanceof String s && s.contains("page=0")) {
				return Duration.ofMinutes(10);
			}
			return jitter(Duration.ofMinutes(60), Duration.ofMinutes(90));
		}

		if ("carts".equals(cacheName)) {
			if (k.startsWith("cart:u:")) {
				return Duration.ofDays(30);
			}
			if (k.startsWith("cart:g:")) {
				return Duration.ofDays(7);
			}
		}

		return Duration.ofSeconds(60);
	}

	private Duration jitter(Duration min, Duration max) {
		long low = min.toMillis();
		long high = max.toMillis();
		long rnd = ThreadLocalRandom.current().nextLong(low, high + 1);

		return Duration.ofMillis(rnd);
	}
}
