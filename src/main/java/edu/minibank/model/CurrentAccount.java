package edu.minibank.model;
public class CurrentAccount extends Account {
 private static final long serialVersionUID=1L;
 private final long overdraftLimit;
 public CurrentAccount(String owner,long opening,long overdraftLimit){super(owner,opening);if(overdraftLimit<0)throw new IllegalArgumentException("Overdraft must be non-negative");this.overdraftLimit=overdraftLimit;}
 public CurrentAccount(String number,String owner,long opening,long overdraftLimit){super(number,owner,opening,true);if(overdraftLimit<0||opening < -overdraftLimit)throw new IllegalArgumentException("Balance exceeds overdraft limit");this.overdraftLimit=overdraftLimit;}
 public long getOverdraftLimit(){return overdraftLimit;} @Override protected long availableToWithdraw(){return getBalance()+overdraftLimit;} @Override public double interestRate(){return 0.0;} @Override public boolean canWithdraw(long amount){return amount>0&&getBalance()-amount>=-overdraftLimit;}
}
