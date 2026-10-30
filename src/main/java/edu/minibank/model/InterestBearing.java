package edu.minibank.model;
public interface InterestBearing { default double yearlyInterest() { return ((Account)this).getBalance() * ((Account)this).interestRate() / 100.0; } }
