package com.Harsh.urlshortener.service;

import com.Harsh.urlshortener.cache.RedisCache;
import com.Harsh.urlshortener.id.IdGenerator;
import com.Harsh.urlshortener.model.UrlMapping;
import com.Harsh.urlshortener.repository.UrlRepository;
import com.Harsh.urlshortener.util.Base62Encoder;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    private final IdGenerator idGenerator;
    private final RedisCache cache;
    private final UrlRepository repo;

    public UrlService(IdGenerator idGenerator, RedisCache cache, UrlRepository repo) {
        this.idGenerator = idGenerator;
        this.cache = cache;
        this.repo = repo;
    }

    public String shorten(String longUrl) {
        long id = idGenerator.nextId();
        String shortCode = Base62Encoder.encode(id);
        repo.save(new UrlMapping(shortCode, longUrl));
        cache.put(shortCode, longUrl);
        return shortCode;
    }

    public String resolve(String shortCode) {
        String cached = cache.get(shortCode);
        if (cached != null) return cached;

        String fromDb = repo.findLongUrl(shortCode);
        if (fromDb != null) cache.put(shortCode, fromDb);
        return fromDb;
    }
}