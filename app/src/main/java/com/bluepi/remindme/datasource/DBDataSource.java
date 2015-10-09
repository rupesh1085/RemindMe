package com.bluepi.remindme.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bluepi.remindme.R;
import com.bluepi.remindme.handler.DBOpenHelper;
import com.bluepi.remindme.model.MessageDetail;
import com.bluepi.remindme.model.ReminderDetail;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by rupesh on 15/9/15.
 */
public class DBDataSource {

    // Database fields
    private SQLiteDatabase database;
    private DBOpenHelper dbHelper;
    private Context con ;
    private String dateFormatArray[];

    public DBDataSource(Context context) {
        dbHelper = new DBOpenHelper(context);
        con = context ;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Adding new message detail

    public void addMessageDetails(MessageDetail mDetail) {

        SQLiteDatabase dataBase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBOpenHelper.SENDER_NAME, mDetail.getSender_name());
        values.put(DBOpenHelper.CREDIT_PRICE, mDetail.getCredit_amount());
        values.put(DBOpenHelper.DEBIT_PRICE, mDetail.getDebit_amount());
        values.put(DBOpenHelper.SPEND_PRICE, mDetail.getSpend_amount());
        values.put(DBOpenHelper.DATE_STAMP, mDetail.getDate());
        // Inserting Row
        dataBase.insert(DBOpenHelper.MESSAGE_DETAIL_TABLE, null, values);
        dataBase.close(); // Closing database connection
    }

    // Getting All Message details
    public List<MessageDetail> getAllMessageDetails() {
        List<MessageDetail> msgList = new ArrayList<MessageDetail>();
        // Select All Query
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DBOpenHelper.MESSAGE_DETAIL_TABLE;
        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MessageDetail mDetail = new MessageDetail();
                mDetail.setSender_name(cursor.getString(0));
                mDetail.setCredit_amount(cursor.getString(1));
                mDetail.setDebit_amount(cursor.getString(2));
                mDetail.setSpend_amount(cursor.getString(3));
                mDetail.setDate(cursor.getString(4));
                // Adding msg to list
                msgList.add(mDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return msgdetail list
        return msgList;
    }

    // Adding new reminder detail

    public void addReminderDetails(ReminderDetail rDetail){

        SQLiteDatabase dataBase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBOpenHelper.SENDER_NAME , rDetail.getSender());
        values.put(DBOpenHelper.DATE_STAMP , rDetail.getDateStamp());
        values.put(DBOpenHelper.REMINDER , rDetail.getReminder());

        dataBase.insert(DBOpenHelper.REMINDER_DETAIL_TABLE , null , values);
        dataBase.close();


    }

    // Getting All Reminder details
    public List<ReminderDetail> getAllReminderDetails() {
        List<ReminderDetail> reminderList = new ArrayList<ReminderDetail>();
        // Select All Query
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DBOpenHelper.REMINDER_DETAIL_TABLE;
        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReminderDetail rDetail = new ReminderDetail();
                rDetail.setSender(cursor.getString(0));
                rDetail.setDateStamp(cursor.getString(1));
                rDetail.setReminder(cursor.getString(2));
                // Adding reminder to list
                reminderList.add(rDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reminderList;
    }




    public LinkedHashMap getGraphData(){
        LinkedHashMap<String , String> a = new LinkedHashMap<>();
        String monthName = "";
        String totalSpend = "";

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        int currentMonth = c.get(Calendar.MONTH) + 1 ;

        if(currentMonth < 5){

            for (int i = currentMonth; i < 1 ; i--) {

                monthName = getMonthName(currentMonth);
                totalSpend = String.valueOf(getSpendPerMonth(currentMonth));

                Log.e("=======mn" , monthName +" / "+totalSpend);

                a.put(monthName , totalSpend);
            }

        }else{

            for (int i = currentMonth; i > (currentMonth-5) ; i--) {

                monthName = getMonthName(i);
                totalSpend = String.valueOf(getSpendPerMonth(i));


                a.put(monthName, totalSpend);
                Log.e("=======mn2", monthName + " / " + totalSpend);
            }

        }
        return  a ;
    }


    public int getParseMonth(String d) {

        int month = 0;
        if (d != null) {
            dateFormatArray = con.getResources().getStringArray(R.array.time_format_list);
            for (String parse : dateFormatArray) {
                SimpleDateFormat sdf = new SimpleDateFormat(parse);
                try {
                    if(! d.equalsIgnoreCase("NA")){

                        Date recordDate = sdf.parse(d);
                        if(recordDate != null){

                            Calendar c = Calendar.getInstance();
                            c.setTime(recordDate);
                            month = c.get(Calendar.MONTH) + 1;

                        }
                    }
                    break ;
                } catch (ParseException e) {
                    e.printStackTrace();

                }
            }
            return  month;
        }
        return  month;
    }

    public String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month-1].substring(0 , 3);
    }

    public double getSpendPerMonth(int month){

        double totalPerMonth = 0.0f ;
        List<MessageDetail>  allMessages = getAllMessageDetails();
        for (int i = 0; i < allMessages.size() ; i++) {

            int mnth = getParseMonth(allMessages.get(i).getDate());
            if(mnth == month){
                totalPerMonth = totalPerMonth + Double.parseDouble(allMessages.get(i).getSpend_amount());
            }
        }
        return totalPerMonth ;
    }
}
