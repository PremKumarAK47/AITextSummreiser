package com.aisummarizer.service;

/**
 * Defines different styles of text summarization.
 * 
 * Each style maps to a specific prompt instruction sent to the AI model,
 * controlling how the summary is generated.
 */
public enum SummaryStyle {

    BRIEF("Summarize the following text in 2-3 concise sentences. "
            + "Focus on the main idea and key takeaway."),

    DETAILED("Provide a detailed summary of the following text. "
            + "Cover all major points, arguments, and conclusions. "
            + "Keep it well-structured and clear."),

    BULLET_POINTS("Summarize the following text as bullet points. "
            + "Each bullet should capture one key point. "
            + "Use clear, concise language. Format with '•' symbols."),

    KEY_FACTS("Extract the key facts and data points from the following text. "
            + "Present them as a numbered list. Focus on concrete information, "
            + "statistics, names, dates, and specific claims."),

    ELI5("Explain the following text in simple terms, as if explaining to "
            + "a 5-year-old. Use simple words, short sentences, and relatable analogies.");

    private final String promptInstruction;

    SummaryStyle(String promptInstruction) {
        this.promptInstruction = promptInstruction;
    }

    /**
     * Returns the AI prompt instruction for this style.
     */
    public String getPromptInstruction() {
        return promptInstruction;
    }

    /**
     * Returns a user-friendly display name.
     */
    public String getDisplayName() {
        return switch (this) {
            case BRIEF -> "Brief Summary (2-3 sentences)";
            case DETAILED -> "Detailed Summary";
            case BULLET_POINTS -> "Bullet Points";
            case KEY_FACTS -> "Key Facts & Data";
            case ELI5 -> "Explain Like I'm 5";
        };
    }
}
