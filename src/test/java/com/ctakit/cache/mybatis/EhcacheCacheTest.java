/**
 * 
 */
package com.ctakit.cache.mybatis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ctakit.cache.mybatis.ehcache3.EhcacheCache;

/**
 * @author lizhenmin
 *
 */
class EhcacheCacheTest {

	private EhcacheCache cache = null;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		  cache = new EhcacheCache("test");
	}

	@Test
	void testPut() {
		String key = "111";
		String expectedValue = "2222";
		cache.putObject(key, expectedValue);
		String value = (String) cache.getObject(key);
		assertEquals(expectedValue, value);
	}

}
