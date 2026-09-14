package com.aisummarizer.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Google Gemini AI implementation of the TextSummarizer interface.
 * 
 * Uses the Gemini REST API to generate text summaries with
 * configurable styles and robust error handling.
 */
public class GeminiSummarizer implements TextSummarizer {

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final String apiKey;
    private final OkHttpClient httpClient;
    private final Gson gson;

    public GeminiSummarizer(String apiKey) {
        this.apiKey = apiKey;
        this.gson = new Gson();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public String summarize(String text, SummaryStyle style) throws SummarizationException {
        if (text == null || text.isBlank()) {
            throw new SummarizationException("Input text cannot be empty.");
        }

        // Build the prompt
        String prompt = buildPrompt(text, style);

        // Build the request body
        String requestBody = buildRequestBody(prompt);

        // Make the API call
        return callGeminiApi(requestBody);
    }

    /**
     * Constructs the full prompt by combining the style instruction with the input text.
     */
    private String buildPrompt(String text, SummaryStyle style) {
        return style.getPromptInstruction() + "\n\n---\n\n" + text;
    }

    /**
     * Builds the JSON request body for the Gemini API.
     */
    private String buildRequestBody(String prompt) {
        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);

        JsonArray parts = new JsonArray();
        parts.add(textPart);

        JsonObject content = new JsonObject();
        content.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(content);

        // Generation config for controlled output
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 0.4);
        generationConfig.addProperty("maxOutputTokens", 2048);

        JsonObject body = new JsonObject();
        body.add("contents", contents);
        body.add("generationConfig", generationConfig);

        return gson.toJson(body);
    }

    /**
     * Sends the request to the Gemini API and extracts the summary from the response.
     */
    private String callGeminiApi(String requestBody) throws SummarizationException {
        String url = GEMINI_API_URL + "?key=" + apiKey;

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(requestBody, JSON_MEDIA_TYPE))
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                handleApiError(response.code(), responseBody);
            }

            return extractSummary(responseBody);

        } catch (IOException e) {
            throw new SummarizationException(
                    "Network error while contacting Gemini API: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts the generated text from the Gemini API JSON response.
     */
    private String extractSummary(String responseBody) throws SummarizationException {
        try {
            JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();

            JsonArray candidates = json.getAsJsonArray("candidates");
            if (candidates == null || candidates.isEmpty()) {
                throw new SummarizationException("No summary was generated. The API returned no candidates.");
            }

            JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
            JsonObject content = firstCandidate.getAsJsonObject("content");
            JsonArray parts = content.getAsJsonArray("parts");

            if (parts == null || parts.isEmpty()) {
                throw new SummarizationException("No summary content found in the API response.");
            }

            return parts.get(0).getAsJsonObject().get("text").getAsString().trim();

        } catch (Exception e) {
            if (e instanceof SummarizationException) throw (SummarizationException) e;
            throw new SummarizationException("Failed to parse API response: " + e.getMessage(), e);
        }
    }

    /**
     * Handles API error responses with user-friendly messages.
     */
    private void handleApiError(int statusCode, String responseBody) throws SummarizationException {
        String message = switch (statusCode) {
            case 400 -> "Bad request. The input text may be too long or contain invalid content.";
            case 401, 403 -> "Authentication failed. Please check your GEMINI_API_KEY.";
            case 404 -> "API endpoint not found. The model may have been deprecated.";
            case 429 -> "Rate limit exceeded. Please wait a moment and try again.";
            case 500, 503 -> "Gemini API server error. Please try again later.";
            default -> "API error (HTTP " + statusCode + "): " + responseBody;
        };
        throw new SummarizationException(message, statusCode);
    }
}
