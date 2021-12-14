package com.windiartaJmartFA.model;

import java.util.Date;

public abstract class Invoice extends Serializable {
    public enum Rating {
        BAD, GOOD, NEUTRAL, NONE;
    }

    public static enum Status {
        CANCELLED, COMPLAINT, DELIVERED, FAILED, FINISHED, ON_DELIVERY, ON_PROGRESS, WAITING_CONFIRMATION;
    }

    public String date;
    public int buyerId;
    public int productId;
    public int complaintId;
    public Rating rating;
}