package com.moviebookingapp.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DatabaseTestController {

    @Value("${spring.datasource.url:NOT_SET}")
    private String databaseUrl;

    @Value("${DATABASE_URL:NOT_SET}")
    private String envDatabaseUrl;

    @GetMapping("/debug/database")
    public Map<String, String> getDatabaseInfo() {
        return Map.of(
            "spring.datasource.url", databaseUrl,
            "DATABASE_URL", envDatabaseUrl,
            "profile", System.getProperty("spring.profiles.active", "default")
        );
    }
}
