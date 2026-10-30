package edu.minibank.service;

import edu.minibank.model.*;
import edu.minibank.exception.*;
import edu.minibank.util.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class BankService {
    private final Repository<Account> repository;
    private final AccountStore store;
    private final Path dataDir;

    public BankService(Repository<Account> repository, AccountStore store, Path dataDir) {
        this.repository = repository;
        this.store = store;
        this.dataDir = dataDir;
        repository.findAll().forEach(store::add);
    }

    public Account openAccount(String owner, String type, long opening) {
        if (owner == null || owner.isBlank()) throw new IllegalArgumentException("Owner name is required");
        Account account = switch (type.toUpperCase()) {
            case "SAVINGS" -> new SavingsAccount(owner, opening, 500);
            case "CURRENT" -> new CurrentAccount(owner, opening, 1000);
            case "FIXED" -> new FixedDepositAccount(owner, opening);
            default -> throw new IllegalArgumentException("Choose SAVINGS, CURRENT, or FIXED");
        };
        repository.save(account);
        store.add(account);
        return account;
    }

    public Account openAccount(String owner, String email, String mobile, String type, long opening) {
        if (owner == null || owner.isBlank()) throw new IllegalArgumentException("Owner name is required");
        if (!Validator.isValidEmail(email)) throw new IllegalArgumentException("Invalid email address");
        if (!Validator.isValidMobile(mobile)) throw new IllegalArgumentException("Invalid mobile number");
        new Customer(owner, email, mobile);
        return openAccount(owner, type, opening);
    }

    public void deposit(String id, long amount) throws BankException {
        Account account = require(id);
        account.deposit(amount);
        log(TransactionType.DEPOSIT, id, null, amount);
    }

    public void withdraw(String id, long amount) throws BankException {
        Account account = require(id);
        account.withdraw(amount);
        log(TransactionType.WITHDRAW, id, null, amount);
    }

    public void transfer(String from, String to, long amount) throws BankException {
        if (from.equals(to)) throw new IllegalArgumentException("Source and destination must differ");
        Account source = require(from), destination = require(to);
        source.transfer(destination, amount);
        log(TransactionType.TRANSFER, from, to, amount);
    }

    public String statement(String id) throws AccountNotFoundException {
        return StatementFormatter.buildStatement(require(id));
    }

    public List<Account> listAccounts() { return store.sortedByBalance(); }

    public Account require(String id) throws AccountNotFoundException {
        Account account = store.get(id);
        if (account == null) throw new AccountNotFoundException(id);
        return account;
    }

    public void process(Transaction transaction) throws BankException {
        switch (transaction.type()) {
            case DEPOSIT -> deposit(transaction.accountNumber(), transaction.amount());
            case WITHDRAW -> withdraw(transaction.accountNumber(), transaction.amount());
            case TRANSFER -> transfer(transaction.accountNumber(), transaction.targetAccountNumber(), transaction.amount());
        }
    }

    private void log(TransactionType type, String id, String to, long amount) throws BankException {
        try {
            TransactionLog.append(dataDir.resolve("logs/transactions.csv"), new Transaction(type, id, to, amount));
            store.addHistory(type + " " + id + (to == null ? "" : " " + to) + " " + amount);
        } catch (IOException e) {
            throw new BankException("Could not write transaction log", e);
        }
    }

    public String report() throws BankException {
        try { return ReportGenerator.generate(dataDir.resolve("logs"), dataDir.resolve("report.txt")); }
        catch (IOException e) { throw new BankException("Could not generate report", e); }
    }

    public void save() throws BankException {
        try { StatePersister.save(store.all().toArray(Account[]::new), dataDir.resolve("accounts.dat")); }
        catch (IOException e) { throw new BankException("Could not save account data", e); }
    }
}
