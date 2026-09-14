package com.aisummarizer.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

/**
 * Application configuration manager.
 * 
 * Loads configuration from .env file or system environment variables.
 * Priority: .env file > System environment variables
 */
public class AppConfig {

    private final String apiKey;

    public AppConfig() {
        String key = null;

        // Try loading from .env file first
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            key = dotenv.get("GEMINI_API_KEY");
        } catch (DotenvException e) {
            System.out.println("⚠️  Could not load .env file: " + e.getMessage());
        }

        // Fallback to system environment variable
        if (key == null || key.isBlank() || key.equals("your_gemini_api_key_here")) {
            key = System.getenv("GEMINI_API_KEY");
        }

        this.apiKey = key;
    }

    /**
     * Returns the configured Gemini API key.
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Checks if the configuration is valid (API key is present).
     */
    public boolean isValid() {
        return apiKey != null && !apiKey.isBlank() && !apiKey.equals("your_gemini_api_key_here");
    }
}
