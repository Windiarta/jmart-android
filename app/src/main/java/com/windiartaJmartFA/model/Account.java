package com.windiartaJmartFA.model;

public class Account extends Serializable{
    public static final String REGEX_EMAIL = "((^(?!\\.)(?!.*\\.$)(?!.*?\\.\\.)[a-zA-Z0-9_.&*_~]+)[@][a-zA-Z0-9.]+)";
    public static final String REGEX_PASSWORD = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?!.\\s)[a-zA-Z0-9]{8,}";
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
