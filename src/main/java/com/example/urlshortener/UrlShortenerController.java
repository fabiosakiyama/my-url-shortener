package com.example.urlshortener;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
public class UrlShortenerController {
    @Value("${app.domain}")
    private String appDomain;

    private final UrlShortenerService urlShortenerService = new UrlShortenerService();

    @GetMapping("/")
    public String shortenForm(Model model) {
        model.addAttribute("shortenRequest", new ShortenRequest());
        return "shorten";
    }

    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortKey) {
        String originalUrl = urlShortenerService.getOriginalUrl(shortKey);
        if (originalUrl == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @PostMapping("/shorten")
    public String shortenUrl(@ModelAttribute("shortenRequest") ShortenRequest shortenRequest, Model model) {
        String originalUrl = shortenRequest.getUrl();
        String shortKey = urlShortenerService.shortenUrl(originalUrl);
        String shortUrl = appDomain + "/" + shortKey;
        model.addAttribute("shortenedUrl", shortUrl);
        return "shorten";
    }

    @Setter
    @Getter
    static class ShortenRequest {
        // standard getters and setters
        private String url;

    }
}
