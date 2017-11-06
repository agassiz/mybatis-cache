package com.ctakit.cache.mybatis;

import javax.cache.configuration.CompleteConfiguration;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;

public class EhcacheCache extends AbstractEhcacheCache {
	public EhcacheCache(String id) {
		super(id);
		cache = myCacheManager.getCache(id);
		if (cache == null) {

			CacheConfiguration<Object, Object> cacheConfiguration = CacheConfigurationBuilder
					.newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(10)).build();
			cache = myCacheManager.createCache(id, Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration));

			Eh107Configuration<Object, Object> configuration = cache.getConfiguration(Eh107Configuration.class);
			configuration.unwrap(CacheConfiguration.class);

			configuration.unwrap(CacheRuntimeConfiguration.class);

			try {
				cache.getConfiguration(CompleteConfiguration.class);
				throw new AssertionError("IllegalArgumentException expected");
			} catch (IllegalArgumentException iaex) {
				// Expected
			}
		} 

	}

	/**
	 * Instantiates a new ehcache cache.
	 *
	 * @param id
	 *            the id
	 */

}