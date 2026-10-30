package edu.minibank.model;
import edu.minibank.model.annotation.MaxLength;
import java.util.Objects;
import java.io.Serializable;
import edu.minibank.model.annotation.Id;
public class Customer implements Cloneable,Serializable {
    private static final long serialVersionUID=1L;
    private static long customerCounter = 100;
    @Id private final String customerId;
    @MaxLength(80) private String name;
    @MaxLength(120) private String email;
    private String mobile;
    private Address address;
    private static String generateCustomerId() { return "CUST" + (++customerCounter); }
    public Customer(String name, String email, String mobile) { this(name,email,mobile,null); }
    public Customer(String name, String email, String mobile, Address address) { this.customerId=generateCustomerId(); this.name=name; this.email=email; this.mobile=mobile; this.address=address; }
    public String getCustomerId(){return customerId;} public String getName(){return name;} public String getEmail(){return email;} public String getMobile(){return mobile;} public Address getAddress(){return address;}
    @Override public Customer clone() { try { Customer c=(Customer)super.clone(); if(address!=null)c.address=address.clone(); return c; } catch(CloneNotSupportedException e){ throw new AssertionError(e); } }
    @Override public String toString(){return customerId+" "+name+" <"+email+">";}
    public static class Address implements Cloneable,Serializable {
        private static final long serialVersionUID=1L;
        private final String line, city, pincode;
        public Address(String line,String city,String pincode){this.line=line;this.city=city;this.pincode=pincode;}
        public String getLine(){return line;} public String getCity(){return city;} public String getPincode(){return pincode;}
        @Override public Address clone(){try{return (Address)super.clone();}catch(CloneNotSupportedException e){throw new AssertionError(e);}}
    }
}
