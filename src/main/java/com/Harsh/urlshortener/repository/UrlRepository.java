package com.Harsh.urlshortener.repository;

import com.Harsh.urlshortener.model.UrlMapping;
import org.springframework.stereotype.Repository;

@Repository
public class UrlRepository {

    private final ShardRouter shardRouter;

    public UrlRepository(ShardRouter shardRouter) {
        this.shardRouter = shardRouter;
    }

    public void save(UrlMapping mapping) {
        var jdbc = shardRouter.getShard(mapping.getShortCode());
        jdbc.update(
            "INSERT INTO url_mappings (short_code, long_url) VALUES (?, ?)",
            mapping.getShortCode(),
            mapping.getLongUrl()
        );
    }

    public String findLongUrl(String shortCode) {
        var jdbc = shardRouter.getShard(shortCode);
        var results = jdbc.queryForList(
            "SELECT long_url FROM url_mappings WHERE short_code = ?",
            String.class,
            shortCode
        );
        return results.isEmpty() ? null : results.get(0);
    }
}