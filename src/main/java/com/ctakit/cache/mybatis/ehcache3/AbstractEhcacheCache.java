package com.ctakit.cache.mybatis.ehcache3;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.locks.ReadWriteLock;

import javax.cache.Caching;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.cache.Cache;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourceType;
import org.ehcache.core.EhcacheManager;

import com.ctakit.cache.mybatis.DummyReadWriteLock;

/**
 * Cache adapter for Ehcache.
 *
 * @author Simone Tripodi
 */
public abstract class AbstractEhcacheCache implements Cache {

	/**
	 * The cache manager reference.
	 */

	protected EhcacheManager ehcacheManager = null;

	protected org.ehcache.Cache<String, Object> cache = null;
	protected CacheConfiguration<?, ?> cacheConfiguration = null;

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
		if (ehcacheManager == null) {
			URL myUrl = getClass().getResource("/ehcache.xml");
			try {
				ehcacheManager = getEhcacheManager(
						Caching.getCachingProvider().getCacheManager(myUrl.toURI(), null, null));
			} catch (URISyntaxException | IllegalAccessException e) {
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
	 * Instantiates a new ehcache cache.
	 *
	 * @param id
	 *            the id
	 */

	public static EhcacheManager getEhcacheManager(Object target) throws IllegalAccessException {

		Field field = FieldUtils.getField(target.getClass(), "ehCacheManager", true);
		Object value = null;
		if (field != null) {
			if (field.isAccessible()) {
				value = field.get(target);
			} else {
				field.setAccessible(true);
				value = field.get(target);
				field.setAccessible(false);
			}
		}
		return (EhcacheManager) value;

		// return FieldUtils.readField(target, fieldName,true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		cache.clear();
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
	@Override
	public int getSize() {
		Long size = cacheConfiguration.getResourcePools().getPoolForResource(ResourceType.Core.HEAP).getSize();
		return size.intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putObject(Object key, Object value) {
		if (key != null && value != null) {
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