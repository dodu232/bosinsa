package com.example.api.infra.cache;

import java.time.Duration;

public interface TtlPolicy {

	Duration resolveTtl(String cacheName, Object key, Object value);
}
