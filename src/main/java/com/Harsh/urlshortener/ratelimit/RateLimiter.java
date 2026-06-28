package com.Harsh.urlshortener.ratelimit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
public class RateLimiter {

    private static final int MAX_REQUESTS = 10;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    private final StringRedisTemplate redis;

    public RateLimiter(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public boolean isAllowed(String ip) {
        try {
            String key = "rate:" + ip;
            Long count = redis.opsForValue().increment(key);
            if (count == 1) redis.expire(key, WINDOW);
            return count <= MAX_REQUESTS;
        } catch (Exception e) {
            return true; // fail-open: if Redis is down, allow the request
        }
    }
}