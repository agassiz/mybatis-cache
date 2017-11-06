package com.ctakit.cache.mybatis;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lujun.chen on 2017/6/21.
 */
public class EhcacheEventLogger implements CacheEventListener<Object, Object> {
  private static final Logger LOGGER = LoggerFactory.getLogger(EhcacheEventLogger.class);

  public EhcacheEventLogger() {
	  LOGGER.info("init EhcacheEventLogger");
}
  @Override
  public void onEvent(CacheEvent<?, ?> event) {
    LOGGER.info("Event: " + event.getType() + " Key: " + event.getKey() + " old value: " + event.getOldValue() + " new value: " + event.getNewValue());
  }
}