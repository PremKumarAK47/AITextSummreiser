package com.aisummarizer.ui;

import com.aisummarizer.service.SummarizationException;
import com.aisummarizer.service.SummaryStyle;
import com.aisummarizer.service.TextSummarizer;
import com.aisummarizer.util.FileReader;

import java.io.IOException;
import java.util.Scanner;

/**
 * Console-based user interface for the AI Text Summarizer.
 * Handles all user interaction, menu display, and input/output flow.
 */
public class ConsoleUI {

    private final TextSummarizer summarizer;
    private final Scanner scanner;

    public ConsoleUI(TextSummarizer summarizer) {
        this.summarizer = summarizer;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the main application loop.
     */
    public void start() {
        System.out.println("  Welcome! This tool uses AI to summarize text for you.\n");

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = readIntInput("  👉 Your choice: ", 1, 3);

            switch (choice) {
                case 1 -> summarizeDirectInput();
                case 2 -> summarizeFromFile();
                case 3 -> {
                    System.out.println("\n  👋 Goodbye! Thanks for using AI Text Summarizer.\n");
                    running = false;
                }
            }
        }
        scanner.close();
    }

    private void showMainMenu() {
        System.out.println("  ┌─────────────────────────────────┐");
        System.out.println("  │         MAIN MENU               │");
        System.out.println("  ├─────────────────────────────────┤");
        System.out.println("  │  1. 📝 Paste text to summarize  │");
        System.out.println("  │  2. 📄 Summarize from file      │");
        System.out.println("  │  3. 🚪 Exit                     │");
        System.out.println("  └─────────────────────────────────┘");
    }

    private void summarizeDirectInput() {
        System.out.println("\n  📝 Enter/paste your text below.");
        System.out.println("  Type 'END' on a new line when done:\n");

        StringBuilder textBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().equalsIgnoreCase("END")) break;
            textBuilder.append(line).append("\n");
        }

        String text = textBuilder.toString().trim();
        if (text.isEmpty()) {
            System.out.println("\n  ⚠️  No text entered. Returning to menu.\n");
            return;
        }

        System.out.println("\n  ✅ Captured " + text.length() + " characters.");
        processSummarization(text);
    }

    private void summarizeFromFile() {
        System.out.print("\n  📄 Enter the file path: ");
        String filePath = scanner.nextLine().trim();

        if (filePath.isEmpty()) {
            System.out.println("  ⚠️  No file path entered.\n");
            return;
        }

        try {
            String text = FileReader.readFile(filePath);
            System.out.println("  ✅ Loaded " + text.length() + " characters from file.");
            processSummarization(text);
        } catch (IOException e) {
            System.out.println("  ❌ Error reading file: " + e.getMessage() + "\n");
        }
    }

    private void processSummarization(String text) {
        SummaryStyle style = selectStyle();
        System.out.println("\n  ⏳ Generating summary with style: " + style.getDisplayName());
        System.out.println("  Please wait...\n");

        try {
            long startTime = System.currentTimeMillis();
            String summary = summarizer.summarize(text, style);
            long elapsed = System.currentTimeMillis() - startTime;

            printSummaryResult(summary, style, elapsed);
        } catch (SummarizationException e) {
            System.out.println("  ❌ Summarization failed: " + e.getMessage() + "\n");
        }
    }

    private SummaryStyle selectStyle() {
        System.out.println("\n  ┌─────────────────────────────────────┐");
        System.out.println("  │       SUMMARY STYLE                 │");
        System.out.println("  ├─────────────────────────────────────┤");

        SummaryStyle[] styles = SummaryStyle.values();
        for (int i = 0; i < styles.length; i++) {
            System.out.printf("  │  %d. %-33s│%n", i + 1, styles[i].getDisplayName());
        }
        System.out.println("  └─────────────────────────────────────┘");

        int choice = readIntInput("  👉 Select style: ", 1, styles.length);
        return styles[choice - 1];
    }

    private void printSummaryResult(String summary, SummaryStyle style, long elapsedMs) {
        System.out.println("  ╔══════════════════════════════════════════════════╗");
        System.out.println("  ║              📋  SUMMARY RESULT                 ║");
        System.out.println("  ╠══════════════════════════════════════════════════╣");
        System.out.printf("  ║  Style: %-41s║%n", style.getDisplayName());
        System.out.printf("  ║  Time:  %-41s║%n", String.format("%.2f seconds", elapsedMs / 1000.0));
        System.out.println("  ╚══════════════════════════════════════════════════╝\n");
        System.out.println(summary);
        System.out.println("\n  " + "─".repeat(50) + "\n");
    }

    private int readIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value;
                System.out.printf("  ⚠️  Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.printf("  ⚠️  Invalid input. Enter a number between %d and %d.%n", min, max);
            }
        }
    }
}
