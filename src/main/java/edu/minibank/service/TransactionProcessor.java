package edu.minibank.service;

import edu.minibank.model.Transaction;
import edu.minibank.exception.BankException;
import java.util.*;
import java.util.concurrent.*;

public class TransactionProcessor implements AutoCloseable {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final BankService service;

    public TransactionProcessor(AccountStore store, BankService service) { this.service = service; }
    public void submit(Runnable task) { executor.execute(task); }
    public List<Future<?>> process(List<Transaction> transactions) {
        List<Future<?>> futures = new ArrayList<>();
        for (Transaction transaction : transactions) futures.add(executor.submit(() -> {
            try { service.process(transaction); }
            catch (BankException e) { throw new CompletionException(e); }
        }));
        return futures;
    }
    public void stop() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) throw new IllegalStateException("Transaction tasks did not stop");
            }
        } catch (InterruptedException e) { executor.shutdownNow(); Thread.currentThread().interrupt(); }
    }
    @Override public void close() { stop(); }
}
