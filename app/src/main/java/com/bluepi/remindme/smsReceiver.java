package com.bluepi.remindme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.bluepi.remindme.Utils.ApplicationUtil;
import com.bluepi.remindme.datasource.DBDataSource;
import com.bluepi.remindme.model.MessageDetail;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class smsReceiver extends BroadcastReceiver {

    private String dateRegex = "([0-9]{2})/([0-9]{2})/([0-9]{2})";
    private String msgRegex = "(?i)Thank you for using.*X+(\\d{4}).*(?:INR|Rs)[\\.,\\s]*([\\d,]*\\.?\\d{2})[ \\.]";
    private String priceregex  = "[0-9]*\\.?[0-9]+" ;
    private Pattern pricePattern , msgPattern , datePattern ;
    private Matcher priceMatcher , msgMatcher , dateMatcher ;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[0]);

            pricePattern = Pattern.compile(priceregex);
            msgPattern = Pattern.compile(msgRegex);
            datePattern = Pattern.compile(dateRegex);

            msgMatcher = msgPattern.matcher(sms.getMessageBody());
            dateMatcher = datePattern.matcher(sms.getMessageBody());
            priceMatcher = pricePattern.matcher(sms.getMessageBody().substring(11));

            if (msgMatcher.find()) {

                if (dateMatcher.find()) {

                    Log.e("==sms body Yes date", dateMatcher.group() +" / / "+sms.getDisplayOriginatingAddress());
                    if (priceMatcher.find()) {

                        Log.e("==sms body Yes INR", new ApplicationUtil().parseIntsAndFloats(sms.getMessageBody().substring(11)).toString() +" /  / "+priceMatcher.group());
                    }
                    DBDataSource db = new DBDataSource(context);
                  //  db.addEvents(new MessageDetail(sms.getDisplayOriginatingAddress().toString() , sms.getMessageBody().substring(11).toString()));

                } else {
                    Log.e("==sms body No date", sms.getMessageBody());
                }
            } else {
                Log.e("==sms body wrong", sms.getMessageBody());
            }
        }
    }



}
