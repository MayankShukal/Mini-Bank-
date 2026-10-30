package edu.minibank.model;
public class SavingsAccount extends Account {
 private static final long serialVersionUID=1L;
 private final long minBalance;
 public SavingsAccount(String owner,long opening,long minBalance){super(owner,opening);if(minBalance<0||opening<minBalance)throw new IllegalArgumentException("Opening balance must meet minimum balance");this.minBalance=minBalance;}
 public SavingsAccount(String number,String owner,long opening,long minBalance){super(number,owner,opening);this.minBalance=minBalance;}
 public long getMinBalance(){return minBalance;} @Override protected long availableToWithdraw(){return getBalance()-minBalance;} @Override public double interestRate(){return 4.0;} @Override public boolean canWithdraw(long amount){return amount>0&&getBalance()-amount>=minBalance;}
}
