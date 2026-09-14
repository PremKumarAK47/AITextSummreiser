package com.aisummarizer.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for reading text files.
 */
public class FileReader {

    public static String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }
        if (!Files.isRegularFile(path)) {
            throw new IOException("Not a regular file: " + filePath);
        }

        long fileSize = Files.size(path);
        if (fileSize > 1_000_000) {
            throw new IOException(String.format("File too large (%.2f MB). Max is 1 MB.", fileSize / 1_000_000.0));
        }

        return Files.readString(path);
    }

    public static boolean isReadable(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isReadable(path) && Files.isRegularFile(path);
    }
}
