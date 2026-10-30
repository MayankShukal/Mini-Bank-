package edu.minibank.service;

import edu.minibank.model.Transaction;
import java.util.ArrayDeque;
import java.util.Queue;

/** A bounded producer-consumer buffer implemented with wait/notifyAll. */
public class TransactionBuffer {
    private final Queue<Transaction> queue = new ArrayDeque<>();
    private final int capacity;
    private boolean closed;

    public TransactionBuffer(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("Capacity must be positive");
        this.capacity = capacity;
    }

    public synchronized void put(Transaction transaction) throws InterruptedException {
        while (queue.size() == capacity && !closed) wait();
        if (closed) throw new IllegalStateException("Buffer is closed");
        queue.add(transaction);
        notifyAll();
    }

    public synchronized Transaction take() throws InterruptedException {
        while (queue.isEmpty() && !closed) wait();
        Transaction transaction = queue.poll();
        notifyAll();
        return transaction;
    }

    public synchronized void close() { closed = true; notifyAll(); }
}
