package edu.minibank.util;

import edu.minibank.model.*;
import edu.minibank.service.TransactionBuffer;

/** Runnable examples for the staged practical exercises: java -jar target/minibank.jar --lab-examples */
public final class LabExamples {
    private LabExamples() {}
    public static void run() {
        Account[] accounts = {
                new SavingsAccount("Example Saver", 5000, 500),
                new CurrentAccount("Example Business", 2000, 1000),
                new FixedDepositAccount("Example Investor", 9000)
        };
        for (Account account : accounts) System.out.println(account + " | interest=" + account.interestRate() + "%");
        if (accounts[0] instanceof SavingsAccount savings) System.out.println("Savings minimum: ₹" + savings.getMinBalance());
        System.out.println("Account equality by number: " + accounts[0].equals(accounts[0]));

        WithdrawRule anonymous = new WithdrawRule() {
            @Override public boolean allow(Account account, long amount) { return account.canWithdraw(amount); }
        };
        WithdrawRule lambda = (account, amount) -> account.canWithdraw(amount);
        System.out.println("Anonymous rule allows 100: " + anonymous.allow(accounts[0], 100));
        System.out.println("Lambda rule allows 100: " + lambda.allow(accounts[0], 100));

        System.out.println("Mobile valid/invalid: " + Validator.isValidMobile("9876543210") + "/" + Validator.isValidMobile("123"));
        System.out.println("Email valid/invalid: " + Validator.isValidEmail("student@example.com") + "/" + Validator.isValidEmail("bad-email"));
        System.out.println("PAN valid/invalid: " + Validator.isValidPan("ABCDE1234F") + "/" + Validator.isValidPan("bad"));
        System.out.println("IFSC valid/invalid: " + Validator.isValidIfsc("SBIN0001234") + "/" + Validator.isValidIfsc("bad"));
        Command command = CommandParser.parse("DEPOSIT AC0001 500");
        System.out.println("Parsed command: " + command.type() + " " + command.accountNumber() + " " + command.amount());

        Account invalid = new CurrentAccount("AC9999", "Validation Sample", -100, 100);
        for (String error : AnnotationValidator.validate(invalid)) System.out.println("Validation: " + error);
        TransactionBuffer buffer = new TransactionBuffer(2);
        Thread producer = new Thread(() -> { try { buffer.put(new Transaction(TransactionType.DEPOSIT, accounts[0].getAccountNumber(), null, 25)); buffer.close(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } });
        Thread consumer = new Thread(() -> { try { System.out.println("Buffered transaction: " + buffer.take()); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } });
        producer.start(); consumer.start();
        try { producer.join(); consumer.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        try (DemoResource resource = new DemoResource()) { System.out.println("Try-with-resources: " + resource.label()); }
    }

    private static final class DemoResource implements AutoCloseable {
        String label() { return "resource opened"; }
        @Override public void close() { System.out.println("Resource closed"); }
    }
}
