package edu.minibank.model;
import edu.minibank.model.annotation.Id;
import edu.minibank.model.annotation.Positive;
import edu.minibank.exception.BankException;
import edu.minibank.exception.InsufficientFundsException;
import edu.minibank.exception.InvalidAmountException;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
public abstract class Account implements Transactable, InterestBearing, Comparable<Account>, Serializable {
    private static final long serialVersionUID=1L;
    private static final AtomicLong COUNTER=new AtomicLong();
    @Id private final String accountNumber;
    private String ownerName;
    @Positive private long balance;
    private boolean active=true;
    protected Account(String ownerName,long openingBalance){this("AC%04d".formatted(COUNTER.incrementAndGet()),ownerName,openingBalance);}
    protected Account(String accountNumber,String ownerName,long openingBalance){this(accountNumber,ownerName,openingBalance,false);}
    protected Account(String accountNumber,String ownerName,long openingBalance,boolean allowNegativeRestore){if(openingBalance<0&&!allowNegativeRestore)throw new IllegalArgumentException("Opening balance cannot be negative");this.accountNumber=Objects.requireNonNull(accountNumber);this.ownerName=Objects.requireNonNull(ownerName);this.balance=openingBalance;try{long suffix=Long.parseLong(accountNumber.substring(2));COUNTER.accumulateAndGet(suffix,Math::max);}catch(RuntimeException ignored){/* Non-generated account numbers do not affect the local sequence. */}}
    public String getAccountNumber(){return accountNumber;} public String getOwnerName(){return ownerName;} public long getBalance(){return balance;} public boolean isActive(){return active;}
    public abstract double interestRate(); public abstract boolean canWithdraw(long amount);
    @Override public synchronized void deposit(long amount) throws InvalidAmountException {if(amount<=0)throw new InvalidAmountException(amount);balance=Math.addExact(balance,amount);}
    @Override public synchronized boolean withdraw(long amount) throws InsufficientFundsException,InvalidAmountException {if(amount<=0)throw new InvalidAmountException(amount);if(!canWithdraw(amount))throw new InsufficientFundsException("Withdrawal denied by account rules or insufficient balance",Math.max(0,amount-availableToWithdraw()));balance=Math.subtractExact(balance,amount);return true;}
    protected long availableToWithdraw(){return getBalance();}
    public void transfer(Account to,long amount) throws BankException {if(to==this)throw new IllegalArgumentException("Cannot transfer to the same account");Account first=accountNumber.compareTo(to.accountNumber)<0?this:to,second=first==this?to:this;synchronized(first){synchronized(second){boolean withdrawn=false;try{withdraw(amount);withdrawn=true;to.deposit(amount);}catch(BankException e){if(withdrawn)balance=Math.addExact(balance,amount);throw e;}finally{/* Locks are released when the synchronized blocks exit. */}}}}
    protected synchronized void setBalanceForRestore(long value){balance=value;}
    public void deactivate(){active=false;}
    @Override public String toString(){return "%s | %s | balance ₹%d | %s".formatted(accountNumber,ownerName,balance,active?"active":"inactive");}
    @Override public boolean equals(Object o){return o instanceof Account a&&accountNumber.equals(a.accountNumber);}
    @Override public int hashCode(){return accountNumber.hashCode();}
    @Override public int compareTo(Account other){return accountNumber.compareTo(other.accountNumber);}
}
