package com.windiartaJmartFA.model;

public class Account extends Serializable{
    public int id;
    public String name;
    public String email;
    public String password;
    public double balance;
    public Store store;

    public String toString(){
        return "name: " + name + "\nemail: " + email + "\npassword: " + password + "\nballance: " + balance + "\nid: "+ id;
    }
}
