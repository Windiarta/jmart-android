package com.windiartaJmartFA.model;

import java.util.ArrayList;
import java.util.Date;

public class Payment extends Invoice {
    public static class Record {
        public Date date;
        public String message;
        public Status status;
    }
    public int id;
    public int productCount;
    public Shipment shipment;
    public ArrayList<Record> history;
}
