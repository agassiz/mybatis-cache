package com.ctakit.cache.mybatis.ehcache3;

import org.ehcache.Cache;

public class EhcacheCache extends AbstractEhcacheCache {
	@SuppressWarnings("unchecked")
	public <K, V> EhcacheCache(String id) {
		super(id);

		cacheConfiguration = ehcacheManager.getRuntimeConfiguration().getCacheConfigurations().get(id);

		if (cacheConfiguration == null) {
			cacheConfiguration = ehcacheManager.getRuntimeConfiguration().getCacheConfigurations().get("default");
		}
		cache = (Cache<String, Object>) ehcacheManager.getCache(id, cacheConfiguration.getKeyType(),
				cacheConfiguration.getValueType());

		if (cache == null) {
			cache = (Cache<String, Object>) ehcacheManager.createCache(id, cacheConfiguration);
		}

	}

}