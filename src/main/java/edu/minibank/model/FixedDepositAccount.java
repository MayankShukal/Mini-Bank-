package edu.minibank.model;
public class FixedDepositAccount extends Account {
 private static final long serialVersionUID=1L;
 public FixedDepositAccount(String owner,long opening){super(owner,opening);} public FixedDepositAccount(String number,String owner,long opening){super(number,owner,opening);}
 @Override public double interestRate(){return 7.0;} @Override public boolean canWithdraw(long amount){return false;}
}
