package edu.minibank.service;

import edu.minibank.model.Account;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AccountStore {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final ArrayList<String> transactionHistory = new ArrayList<>();
    public Account add(Account account) { accounts.put(account.getAccountNumber(), account); return account; }
    public Account get(String number) { return accounts.get(number); }
    public Collection<Account> getAll() { return List.copyOf(accounts.values()); }
    public Collection<Account> all() { return getAll(); }
    public boolean remove(String number) { return accounts.remove(number) != null; }
    public synchronized void addHistory(String entry) { transactionHistory.add(entry); }
    public synchronized ArrayList<String> getTransactionHistory() { return new ArrayList<>(transactionHistory); }
    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> sorted = new ArrayList<>(accounts.values());
        sorted.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return sorted;
    }
}
