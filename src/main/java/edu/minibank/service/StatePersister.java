package edu.minibank.service;

import edu.minibank.model.Account;
import java.io.*;
import java.nio.file.*;

public final class StatePersister {
    private StatePersister() {}

    public static void save(Account[] accounts, Path file) throws IOException {
        Path absolute = file.toAbsolutePath();
        Files.createDirectories(absolute.getParent());
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(absolute))) {
            out.writeObject(accounts);
        }
    }

    public static Account[] load(Path file) throws IOException {
        if (!Files.exists(file)) return new Account[0];
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            try { return (Account[]) in.readObject(); }
            catch (ClassNotFoundException | ClassCastException e) { throw new IOException("Saved account data is incompatible", e); }
        }
    }
}
