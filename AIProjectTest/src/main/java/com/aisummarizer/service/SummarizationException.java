package com.aisummarizer.service;

/**
 * Custom exception for summarization failures.
 * 
 * Wraps API errors, network issues, and other failures
 * that may occur during the summarization process.
 */
public class SummarizationException extends Exception {

    private final int statusCode;

    public SummarizationException(String message) {
        super(message);
        this.statusCode = -1;
    }

    public SummarizationException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
    }

    public SummarizationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Returns the HTTP status code if this was an API error, or -1 otherwise.
     */
    public int getStatusCode() {
        return statusCode;
    }
}
