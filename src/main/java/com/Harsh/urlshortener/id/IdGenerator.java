package com.Harsh.urlshortener.id;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

    private final StringRedisTemplate redis;

    public IdGenerator(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public long nextId() {
        try {
            Long id = redis.opsForValue().increment("url:id:counter");
            return id != null ? id : System.currentTimeMillis();
        } catch (Exception e) {
            return System.currentTimeMillis(); // fallback if Redis is down
        }
    }
}