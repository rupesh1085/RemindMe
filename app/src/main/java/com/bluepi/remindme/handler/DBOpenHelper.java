package com.bluepi.remindme.handler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rupesh on 15/9/15.
 */
public class DBOpenHelper extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Remindme";
    // Events Message Table Columns names
    public static final String SENDER_NAME = "sender_name";
    public static final String CREDIT_PRICE = "credit_amount";
    public static final String DEBIT_PRICE = "debit_amount";
    public static final String SPEND_PRICE = "spend_amount";
    public static final String DATE_STAMP = "date";
    public static final String REMINDER = "reminder";
    // Events table name
    public static final String MESSAGE_DETAIL_TABLE = "messeage_detail";
    public static final String REMINDER_DETAIL_TABLE = "reminder_detail";

    public static final String CREATE_MESSAGE_TABLE = "CREATE TABLE " + MESSAGE_DETAIL_TABLE + "("
            + SENDER_NAME + " TEXT," + CREDIT_PRICE + " TEXT," + DEBIT_PRICE + " TEXT," + SPEND_PRICE + " TEXT," + DATE_STAMP + " TEXT" +")";

    public static final String CREATE_REMINDER_TABLE = "CREATE TABLE " + REMINDER_DETAIL_TABLE + "("
            + SENDER_NAME + " TEXT," + DATE_STAMP + " TEXT," + REMINDER + " TEXT" +")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.e("======executed" , CREATE_MESSAGE_TABLE);
        sqLiteDatabase.execSQL(CREATE_MESSAGE_TABLE);
        sqLiteDatabase.execSQL(CREATE_REMINDER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MESSAGE_DETAIL_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + REMINDER_DETAIL_TABLE);
        // Create tables again
        onCreate(sqLiteDatabase);

    }
}
