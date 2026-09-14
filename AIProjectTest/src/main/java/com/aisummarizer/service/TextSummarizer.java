package com.aisummarizer.service;

/**
 * Interface for text summarization services.
 * 
 * Provides abstraction over different AI providers, making it easy
 * to swap between Gemini, OpenAI, or other models.
 */
public interface TextSummarizer {

    /**
     * Summarizes the given text.
     *
     * @param text   the text to summarize
     * @param style  the summarization style (e.g., BRIEF, DETAILED, BULLET_POINTS)
     * @return the generated summary
     * @throws SummarizationException if the summarization fails
     */
    String summarize(String text, SummaryStyle style) throws SummarizationException;
}
