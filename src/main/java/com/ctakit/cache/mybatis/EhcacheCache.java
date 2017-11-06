package com.ctakit.cache.mybatis;

import org.apache.ibatis.cache.CacheKey;
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
	
	
	public static void main(String[] args) {
		

		
		EhcacheCache d = new EhcacheCache("tttt");
		
		  CacheKey cacheKey = new CacheKey();
		    cacheKey.update("1111");
		    
		
		
		d.putObject(cacheKey, "22222");
		System.out.println("test"+d.getObject(cacheKey));
	}


}