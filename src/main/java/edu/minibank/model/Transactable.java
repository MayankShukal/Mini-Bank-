package edu.minibank.model;
import edu.minibank.exception.InsufficientFundsException;
import edu.minibank.exception.InvalidAmountException;
public interface Transactable { void deposit(long amount) throws InvalidAmountException; boolean withdraw(long amount) throws InsufficientFundsException,InvalidAmountException; }
