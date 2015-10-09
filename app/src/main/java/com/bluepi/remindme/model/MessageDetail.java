package com.bluepi.remindme.model;

/**
 * Created by rupesh on 15/9/15.
 */
public class MessageDetail {

    private String sender_name;
    private String credit_amount;
    private String debit_amount;
    private String spend_amount;
    private String date ;

    public MessageDetail() {

    }

    public MessageDetail(String sender_name, String credit_price, String debit_price, String spend_price, String date) {
        this.sender_name = sender_name;
        this.credit_amount = credit_price;
        this.debit_amount = debit_price;
        this.spend_amount = spend_price;
        this.date = date;
    }

    public String getSpend_amount() {
        return spend_amount;
    }

    public void setSpend_amount(String spend_amount) {
        this.spend_amount = spend_amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }


    public String getCredit_amount() {
        return credit_amount;
    }

    public void setCredit_amount(String credit_amount) {
        this.credit_amount = credit_amount;
    }

    public String getDebit_amount() {
        return debit_amount;
    }

    public void setDebit_amount(String debit_amount) {
        this.debit_amount = debit_amount;
    }



}
