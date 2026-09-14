# 🤖 AI Text Summarizer

A **Java command-line application** that uses **Google Gemini AI** to generate intelligent summaries of text. Paste text directly or load from files, and choose from multiple summarization styles.

---

## ✨ Features

- **Multiple Summary Styles**:
  - 📌 Brief Summary (2-3 sentences)
  - 📖 Detailed Summary
  - 🔹 Bullet Points
  - 📊 Key Facts & Data
  - 👶 Explain Like I'm 5 (ELI5)
- **Dual Input Methods**: Paste text directly or load from a file
- **Gemini AI Powered**: Uses Google's latest Gemini 2.0 Flash model
- **Clean CLI Interface**: Interactive menus with box-drawing characters
- **Error Handling**: Robust error handling with user-friendly messages
- **Configurable**: API key via `.env` file or system environment variable

---

## 📁 Project Structure

```
AIProjectTest/
├── pom.xml                          # Maven build configuration
├── .env.example                     # API key template
├── .gitignore
├── samples/
│   └── sample_text.txt              # Sample text for testing
├── src/main/java/com/aisummarizer/
│   ├── App.java                     # Main entry point
│   ├── config/
│   │   └── AppConfig.java           # Configuration manager
│   ├── service/
│   │   ├── TextSummarizer.java      # Summarizer interface
│   │   ├── GeminiSummarizer.java    # Gemini AI implementation
│   │   ├── SummaryStyle.java        # Summary style enum
│   │   └── SummarizationException.java  # Custom exception
│   ├── ui/
│   │   └── ConsoleUI.java           # CLI user interface
│   └── util/
│       └── FileReader.java          # File reading utility
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **Google Gemini API Key** ([Get it free here](https://aistudio.google.com/app/apikey))

### Setup

1. **Clone/navigate to the project:**
   ```bash
   cd AIProjectTest
   ```

2. **Configure your API key:**
   ```bash
   # Copy the example and add your key
   cp .env.example .env
   # Edit .env and replace 'your_gemini_api_key_here' with your actual key
   ```

3. **Build the project:**
   ```bash
   mvn clean compile
   ```

4. **Run the application:**
   ```bash
   mvn exec:java
   ```

### Alternative: Build & Run as JAR

```bash
mvn clean package
java -jar target/ai-text-summarizer-1.0.0.jar
```

---

## 🎮 Usage

```
╔══════════════════════════════════════════════════════════╗
║              🤖  AI TEXT SUMMARIZER  🤖                 ║
║          Powered by Google Gemini AI                    ║
╚══════════════════════════════════════════════════════════╝

  ┌─────────────────────────────────┐
  │         MAIN MENU               │
  ├─────────────────────────────────┤
  │  1. 📝 Paste text to summarize  │
  │  2. 📄 Summarize from file      │
  │  3. 🚪 Exit                     │
  └─────────────────────────────────┘
```

### Option 1: Paste Text
- Select option 1, paste your text, and type `END` on a new line when done
- Choose a summary style and get your AI-generated summary

### Option 2: From File
- Select option 2 and enter the path to a text file
- Try the included sample: `samples/sample_text.txt`

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 17 | Core language (text blocks, switch expressions) |
| Maven | Build & dependency management |
| OkHttp | HTTP client for API calls |
| Gson | JSON parsing |
| dotenv-java | Environment variable management |
| Google Gemini | AI text generation |

---

## 📜 License

This project is for educational and personal use.
