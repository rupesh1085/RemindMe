package com.bluepi.remindme.model;

/**
 * Created by rupesh on 2/10/15.
 */
public class ReminderDetail {


    private String sender ;
    private String dateStamp ;
    private String reminder ;

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }
}
