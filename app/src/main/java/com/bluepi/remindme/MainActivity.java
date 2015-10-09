package com.bluepi.remindme;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bluepi.remindme.adapter.MessagelistAdapter;
import com.bluepi.remindme.datasource.DBDataSource;
import com.bluepi.remindme.handler.DBOpenHelper;
import com.bluepi.remindme.model.MessageDetail;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends Activity {

    private ListView msgList;
    private TextView alertText;
    private DBDataSource db;
    private File database;
    private MessagelistAdapter adapter;
    private List<MessageDetail> msgDetail;

/*    <item>dd/MM/yyyy' at 'hh:mma</item>
    <item>dd MMM, yyyy hh:mma</item>
    ([0-9]{2})\s([0-9]{3}).\s([0-9]{4}\s([0-9]{2}):([0-9]{2})(?:am|pm)
    <item>dd-MM-yyyy'/'HH:mm:ss</item>
    <item>ddMMM, hh:mm a</item>*/

    private String dateRegex;     // "([0-9]{2})/([0-9]{2})/([0-9]{2})";
    private String dateRegexArray[];
    private String msgRegex; //= "(?i)Thank you for using.*X+(\\d{4}).*(?:INR|Rs)[\\.,\\s]*([\\d,]*\\.?\\d{2})[ \\.]";
    private String msgRegexArray[];
    private String priceregex = "[0-9]*\\.?[0-9]+";

    private Pattern pricePattern, msgPattern, datePattern;
    private Matcher priceMatcher, msgMatcher, dateMatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msgList = (ListView) findViewById(R.id.msglist);
        alertText = (TextView) findViewById(R.id.alerttext);

        msgRegexArray = getResources().getStringArray(R.array.regex_message_list);
        dateRegexArray = getResources().getStringArray(R.array.regex_times_list);

        database = getApplicationContext().getDatabasePath("Remindme");
        db = new DBDataSource(MainActivity.this);


        if (!database.exists()) {
            // Database does not exist so copy it from assets here
            alertText.setVisibility(View.VISIBLE);
            getAllSms();
            Log.i("Database", "Not Found");
        } else {
            Log.i("Database", "Found");
            List<MessageDetail> msgDetail = db.getAllMessageDetails();
            adapter = new MessagelistAdapter(getApplicationContext(), R.layout.msglistrow, msgDetail);

            msgList.setAdapter(adapter);

        }

    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAllSms() {

        Uri inboxURI = Uri.parse("content://sms/inbox");
        String[] reqCols = new String[]{"_id", "address", "body"};
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(inboxURI, reqCols, null, null, null);


        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {

                String address = c.getString(c.getColumnIndexOrThrow("address"));
                String body = c.getString(c.getColumnIndexOrThrow("body"));
                for (int j = 0; j < msgRegexArray.length; j++) {
                    msgRegex = msgRegexArray[j];
                    pricePattern = Pattern.compile(priceregex);
                    Log.e("============" + j, msgRegex);
                    msgPattern = Pattern.compile(msgRegex);
                    msgMatcher = msgPattern.matcher(body);

                    if (msgMatcher.find()) {


                       /* for (int k = 0; k < dateRegexArray.length; k++) {

                            dateRegex = dateRegexArray[k];
                            Log.e("============" + k, dateRegex);
                            datePattern = Pattern.compile(dateRegex);

                            dateMatcher = datePattern.matcher(body);
                            priceMatcher = pricePattern.matcher(body);

                            if (dateMatcher.find()) {*/

                                DBDataSource db = new DBDataSource(getApplicationContext());
                               // db.addEvents(new MessageDetail(address, body));

                                Log.e("=====", c.getString(c.getColumnIndexOrThrow("address")));
                                Log.e("=====", c.getString(c.getColumnIndexOrThrow("body")));
                          //  }
                      //  }
                    }
                }

            }

            c.moveToNext();
        }

         /*else {
         throw new RuntimeException("You have no SMS in " + "inbox");
         }*/
        c.close();
        alertText.setVisibility(View.GONE);
        List<MessageDetail> msgDetail = db.getAllMessageDetails();
        adapter = new MessagelistAdapter(getApplicationContext(), R.layout.msglistrow, msgDetail);
    }
}
