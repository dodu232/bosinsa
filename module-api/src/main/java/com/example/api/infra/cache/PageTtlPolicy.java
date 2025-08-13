package com.example.api.infra.cache;

import java.time.Duration;

public class PageTtlPolicy implements TtlPolicy {


	private final Duration firstPageTtl = Duration.ofMinutes(10);
	private final Duration otherTtl = Duration.ofMinutes(1);

	@Override
	public Duration resolveTtl(String cacheName, Object key, Object value) {
		if (key instanceof String s && s.contains("page=0")) {
			return firstPageTtl;
		}
		return otherTtl;
	}


}
