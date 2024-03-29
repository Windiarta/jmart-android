package com.windiartaJmartFA.model;

public class Product extends Serializable {
    public int id;
    public int accountId;
    public ProductCategory category;
    public boolean conditionUsed;
    public double discount;
    public String name;
    public double price;
    public byte shipmentPlans;
    public int weight;
}
