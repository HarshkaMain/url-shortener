package com.Harsh.urlshortener.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Configuration
public class DataSourceConfig {

    @Value("${shard.0.url}") private String shard0Url;
    @Value("${shard.0.username}") private String shard0Username;
    @Value("${shard.0.password}") private String shard0Password;

    @Value("${shard.1.url}") private String shard1Url;
    @Value("${shard.1.username}") private String shard1Username;
    @Value("${shard.1.password}") private String shard1Password;

    @Bean
    public List<JdbcTemplate> shardJdbcTemplates() {
        HikariDataSource ds0 = new HikariDataSource();
        ds0.setJdbcUrl(shard0Url);
        ds0.setUsername(shard0Username);
        ds0.setPassword(shard0Password);

        HikariDataSource ds1 = new HikariDataSource();
        ds1.setJdbcUrl(shard1Url);
        ds1.setUsername(shard1Username);
        ds1.setPassword(shard1Password);

        return List.of(new JdbcTemplate(ds0), new JdbcTemplate(ds1));
    }
}