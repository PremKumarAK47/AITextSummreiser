package com.aisummarizer;

import com.aisummarizer.config.AppConfig;
import com.aisummarizer.service.GeminiSummarizer;
import com.aisummarizer.service.TextSummarizer;
import com.aisummarizer.ui.ConsoleUI;

/**
 * AI Text Summarizer - Command Line Application
 * 
 * A Java CLI tool that leverages Google Gemini AI to generate
 * concise summaries of text input or text files.
 */
public class App {

    private static final String BANNER = """
            
            ╔══════════════════════════════════════════════════════════╗
            ║              🤖  AI TEXT SUMMARIZER  🤖                 ║
            ║          Powered by Google Gemini AI                     ║
            ╚══════════════════════════════════════════════════════════╝
            """;

    public static void main(String[] args) {
        System.out.println(BANNER);

        // Load configuration
        AppConfig config = new AppConfig();
        if (!config.isValid()) {
            System.err.println("❌ Error: GEMINI_API_KEY not found!");
            System.err.println("   Please create a .env file with your API key.");
            System.err.println("   See .env.example for reference.");
            System.exit(1);
        }

        // Initialize services
        TextSummarizer summarizer = new GeminiSummarizer(config.getApiKey());
        ConsoleUI ui = new ConsoleUI(summarizer);

        // Run the application
        ui.start();
    }
}
