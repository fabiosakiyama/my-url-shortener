package com.example.urlshortener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UrlShortenerService {

    private static final String URL_PREFIX = "amumu";
    private final ConcurrentHashMap<String, String> urlMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    // Simple Base62 Encoder
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String shortenUrl(String originalUrl) {
        int count = counter.incrementAndGet();
        String encodedId = base62Encode(count);
        String shortKey = URL_PREFIX + encodedId;
        urlMap.put(shortKey, originalUrl);
        return shortKey;
    }

    public String getOriginalUrl(String shortKey) {
        return urlMap.get(shortKey);
    }

    private String base62Encode(int count) {
        StringBuilder shortUrl = new StringBuilder();
        while (count > 0) {
            shortUrl.insert(0, BASE62.charAt(count % 62));
            count /= 62;
        }
        return shortUrl.toString();
    }
}
