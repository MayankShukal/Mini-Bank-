package edu.minibank.exception;
public class InvalidAmountException extends BankException {public InvalidAmountException(long amount){super("Amount must be positive: "+amount);}}
