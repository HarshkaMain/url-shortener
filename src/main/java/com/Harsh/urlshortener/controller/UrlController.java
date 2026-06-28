package com.Harsh.urlshortener.controller;

import com.Harsh.urlshortener.ratelimit.RateLimiter;
import com.Harsh.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
public class UrlController {

    private final UrlService urlService;
    private final RateLimiter rateLimiter;

    public UrlController(UrlService urlService, RateLimiter rateLimiter) {
        this.urlService = urlService;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shorten(@RequestBody Map<String, String> body,
                                     HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (!rateLimiter.isAllowed(ip)) {
            return ResponseEntity.status(429).body("Rate limit exceeded");
        }
        String shortCode = urlService.shorten(body.get("longUrl"));
        return ResponseEntity.ok(Map.of("shortUrl", "http://localhost:8080/" + shortCode));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirect(@PathVariable String shortCode) {
        String longUrl = urlService.resolve(shortCode);
        if (longUrl == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }
}