package com.Harsh.urlshortener.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShardRouter {

    private final List<JdbcTemplate> shards;

    public ShardRouter(List<JdbcTemplate> shards) {
        this.shards = shards;
    }

    public JdbcTemplate getShard(String shortCode) {
        int shardId = Math.abs(shortCode.hashCode()) % shards.size();
        return shards.get(shardId);
    }
}