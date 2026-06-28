package com.Harsh.urlshortener.cache;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
public class RedisCache {

    private static final String PREFIX = "url:cache:";
    private static final Duration TTL = Duration.ofHours(24);

    private final StringRedisTemplate redis;

    public RedisCache(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void put(String shortCode, String longUrl) {
        try {
            redis.opsForValue().set(PREFIX + shortCode, longUrl, TTL);
        } catch (Exception ignored) {}
    }

    public String get(String shortCode) {
        try {
            return redis.opsForValue().get(PREFIX + shortCode);
        } catch (Exception e) {
            return null;
        }
    }
}