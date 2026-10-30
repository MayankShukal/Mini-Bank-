package edu.minibank.util;
import edu.minibank.model.Account;
public final class StatementFormatter {private StatementFormatter(){} public static String buildStatement(Account a){return new StringBuilder("MINIBANK ACCOUNT STATEMENT\n-------------------------\nAccount: ").append(a.getAccountNumber()).append("\nOwner: ").append(a.getOwnerName()).append("\nBalance: ₹").append(a.getBalance()).append("\nStatus: ").append(a.isActive()?"Active":"Inactive").append("\nInterest rate: ").append(a.interestRate()).append("%\n").toString();}}
