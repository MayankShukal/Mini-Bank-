package edu.minibank.service;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public final class ReportGenerator {
    private ReportGenerator() {}
    public static String generate(Path logs, Path output) throws IOException {
        long deposits = 0, withdrawals = 0;
        StringBuilder details = new StringBuilder();
        if (Files.isDirectory(logs)) {
            try (DirectoryStream<Path> files = Files.newDirectoryStream(logs)) {
                for (Path file : files) {
                    if (!Files.isRegularFile(file)) continue;
                    BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
                    details.append(file.getFileName()).append(" (bytes=").append(attributes.size())
                            .append(", modified=").append(attributes.lastModifiedTime()).append(")\n");
                    try (BufferedReader reader = Files.newBufferedReader(file)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] columns = line.split(",");
                            try {
                                if (columns.length == 3) {
                                    long amount = Long.parseLong(columns[2].trim());
                                    if (columns[0].trim().equals("DEPOSIT")) deposits += amount;
                                    else if (columns[0].trim().equals("WITHDRAW")) withdrawals += amount;
                                } else if (columns.length >= 5) {
                                    long amount = Long.parseLong(columns[4].trim());
                                    if (columns[1].trim().equals("DEPOSIT")) deposits += amount;
                                    else if (columns[1].trim().equals("WITHDRAW")) withdrawals += amount;
                                }
                            } catch (NumberFormatException ignored) { /* Ignore malformed log entries. */ }
                        }
                    }
                }
            }
        }
        String report = "Transaction report\nDeposits: ₹" + deposits + "\nWithdrawals: ₹" + withdrawals
                + "\nNet change: ₹" + (deposits - withdrawals) + "\nLog files:\n" + details;
        Path absolute = output.toAbsolutePath();
        Files.createDirectories(absolute.getParent());
        Files.writeString(absolute, report);
        return report;
    }
}
