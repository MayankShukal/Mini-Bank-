package edu.minibank.model;
public record Transaction(TransactionType type,String accountNumber,String targetAccountNumber,long amount) {}
