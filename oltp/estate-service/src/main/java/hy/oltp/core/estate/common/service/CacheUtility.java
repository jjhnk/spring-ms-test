package hy.oltp.core.estate.common.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import hy.oltp.core.estate.common.config.RedisCommonConfig;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public final class CacheUtility<T> {
  private final boolean isRedisEnabled;
  private final RedisTemplate<String, T> redisTemplate;
  private final ValueOperations<String, T> valueOps;
  private static final String COMMON_KEY = "estate:";

  public CacheUtility(RedisCommonConfig commonConfig, RedisTemplate<String, T> redisTemplate) {
    this.isRedisEnabled = commonConfig.isRedisEnabled();
    this.redisTemplate = redisTemplate;
    this.valueOps = redisTemplate.opsForValue();
  }

  public boolean isCacheAvailable(HttpHeaders headers) {
    String cacheControl = headers.getCacheControl();

    if (!isRedisEnabled) {
      return false;
    }

    var cacheAvailable = cacheControl == null || cacheControl.isEmpty();
    cacheAvailable &=
      !(cacheControl.contains("no-cache") || cacheControl.contains("no-store") || cacheControl.contains("private"));

    return cacheAvailable;
  }

  public T getFromCache(String serviceKey, int id) {
    var key = serviceKey + id;
    return valueOps.get(COMMON_KEY + key);
  }

  public T safeGetFromCache(HttpHeaders headers, String serviceKey, int id) {
    if (!isRedisEnabled || !isCacheAvailable(headers)) {
      return null;
    }

    log.info("Found {}:{} in cache", serviceKey, id);
    return getFromCache(serviceKey, id);
  }

  public List<T> getListFromCache(String serviceKey) {
    Set<String> keys = redisTemplate.keys(COMMON_KEY + serviceKey + "*");
    if (keys == null || keys.isEmpty()) {
      return List.of();
    }

    return valueOps.multiGet(keys);
  }

  public List<T> safeGetListFromCache(HttpHeaders headers, String serviceKey) {
    if (!isRedisEnabled || !isCacheAvailable(headers)) {
      return List.of();
    }

    log.info("Found {} in cache", serviceKey);
    return getListFromCache(serviceKey);
  }

  public void addToCache(String key, T value) {
    valueOps.set(COMMON_KEY + key, value);
  }

  public boolean safeAddToCache(HttpHeaders headers, String key, T value) {
    if (!isRedisEnabled || !isCacheAvailable(headers)) {
      return false;
    }

    addToCache(key, value);
    return true;
  }

  public void removeFromCache(String key) {
    valueOps.getOperations()
      .delete(COMMON_KEY + key);
  }

  public void safeRemoveFromCache(HttpHeaders headers, String key) {
    if (!isRedisEnabled || !isCacheAvailable(headers)) {
      return;
    }

    log.info("Removing {} from cache", key);
    removeFromCache(key);
  }
}
