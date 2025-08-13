package com.example.api.infra.cache;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class PageTtlPolicy implements TtlPolicy {


	private final Duration firstPageTtl = Duration.ofMinutes(10);
	private final int othersMin = 60;
	private final int othersMax = 90;


	@Override
	public Duration resolveTtl(String cacheName, Object key, Object value) {
		if (key instanceof String s && s.contains("page=0")) {
			return firstPageTtl;
		}
		int secs = ThreadLocalRandom.current().nextInt(othersMin, othersMax);
		return Duration.ofSeconds(secs);
	}

}
