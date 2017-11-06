package com.ctakit.cache.mybatis;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.locks.ReadWriteLock;

import javax.cache.CacheManager;
import javax.cache.Caching;

import org.apache.ibatis.cache.Cache;
import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.config.ResourceType;
import org.ehcache.jsr107.Eh107Configuration;

/**
 * Cache adapter for Ehcache.
 *
 * @author Simone Tripodi
 */
public abstract class AbstractEhcacheCache implements Cache {

	/**
	 * The cache manager reference.
	 */

	protected CacheManager myCacheManager = null;

	protected javax.cache.Cache<Object, Object> cache = null;
	
	 private final ReadWriteLock readWriteLock = new DummyReadWriteLock();


	/**
	 * The cache id (namespace).
	 */
	protected final String id;

	protected void init() {
	}

	/**
	 * Instantiates a new abstract ehcache cache.
	 *
	 * @param id
	 *            the chache id (namespace)
	 */
	public AbstractEhcacheCache(final String id) {
		if (myCacheManager == null) {
			URL myUrl = getClass().getResource("/ehcache.xml");
			try {
				myCacheManager = (CacheManager) Caching.getCachingProvider().getCacheManager(myUrl.toURI(), null, null);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}

		if (id == null) {
			throw new IllegalArgumentException("Cache instances require an ID");
		}
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		cache.clear();
		;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(Object key) {
		Object cachedElement = cache.get(key.toString());
		if (cachedElement == null) {
			return null;
		}
		return cachedElement;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int getSize() {
		 CacheRuntimeConfiguration<Object, Object> ehcacheConfig = (CacheRuntimeConfiguration<Object, Object>)cache.getConfiguration(
			    Eh107Configuration.class).unwrap(CacheRuntimeConfiguration.class); 
		 Long size = ehcacheConfig.getResourcePools().getPoolForResource(ResourceType.Core.HEAP).getSize(); 
		return size.intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putObject(Object key, Object value) {
		if(key!=null&&value!=null) {
			cache.put(key.toString(), value);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object removeObject(Object key) {
		Object obj = getObject(key);
		cache.remove(key.toString());
		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	public void unlock(Object key) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Cache)) {
			return false;
		}

		Cache otherCache = (Cache) obj;
		return id.equals(otherCache.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public ReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "EHCache {" + id + "}";
	}

}