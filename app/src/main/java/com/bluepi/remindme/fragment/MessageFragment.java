package com.bluepi.remindme.fragment;

import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluepi.remindme.R;
import com.bluepi.remindme.Utils.ApplicationUtil;
import com.bluepi.remindme.adapter.MessagelistAdapter;
import com.bluepi.remindme.datasource.DBDataSource;
import com.bluepi.remindme.model.MessageDetail;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rupesh on 12/7/15.
 */
public class MessageFragment extends Fragment {

    private View rootView;
    private ListView msgList;
    private TextView alertText;
    private DBDataSource db;
    private File database;

    private MessagelistAdapter adapter;
    private List<MessageDetail> msgDetail;

    private String dateRegex;
    private String dateRegexArray[];
    private String msgRegex;
    private String msgRegexArray[];
    private String priceregex = "[0-9]*\\.?[0-9]+";
    private Pattern pricePattern, msgPattern, datePattern;
    private Matcher priceMatcher, msgMatcher, dateMatcher;
    private ProgressBar progressbar;
    private Animation animation;
    private String price;
    private String dateFormatArray[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_main, container, false);

        animation = new AlphaAnimation(1, 0);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);


        msgList = (ListView) rootView.findViewById(R.id.msglist);
        alertText = (TextView) rootView.findViewById(R.id.alerttext);
        progressbar = (ProgressBar) rootView.findViewById(R.id.progressbar);

        msgRegexArray = getResources().getStringArray(R.array.regex_message_list);
        dateRegexArray = getResources().getStringArray(R.array.regex_times_list);
        dateFormatArray = getResources().getStringArray(R.array.time_format_list);

        database = getActivity().getApplicationContext().getDatabasePath("Remindme");
        db = new DBDataSource(getActivity());


        if (!database.exists()) {

            alertText.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.VISIBLE);
            new MyAsync().execute();
            alertText.setText("Wait..Reading Inbox");
            alertText.setAnimation(animation);

        } else {

            List<MessageDetail> msgDetail = db.getAllMessageDetails();
            if (msgDetail.size() > 0) {

                adapter = new MessagelistAdapter(getActivity(), R.layout.msglistrow, msgDetail);
                msgList.setAdapter(adapter);
            } else {
                alertText.setVisibility(View.VISIBLE);
                alertText.setText("No relevant data available");
            }
        }

        return rootView;

    }

    private class MyAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            Uri inboxURI = Uri.parse("content://sms/");
            String[] reqCols = new String[]{"_id", "address", "body"};
            ContentResolver cr = getActivity().getContentResolver();
            Cursor c = cr.query(inboxURI, reqCols, null, null, null);
            DBDataSource db = new DBDataSource(getActivity());

            while (c.moveToNext()) {
                String body = c.getString(c.getColumnIndexOrThrow("body"));

                for (int j = 0; j < msgRegexArray.length; j++) {
                    msgRegex = msgRegexArray[j];
                    msgPattern = Pattern.compile(msgRegex);
                    msgMatcher = msgPattern.matcher(body);

                    if (body.matches(msgRegex) || msgMatcher.find()) {
                        String address = c.getString(c.getColumnIndexOrThrow("address"));
                        if (address.equalsIgnoreCase("RM-HDFCBK") || address.equalsIgnoreCase("VM-HDFCBK") || address.equalsIgnoreCase("DM-HDFCBK")) {

                            String dateRegex = "Rs.([\\d,\\,]*\\.\\d{2})";
                            Pattern dateRegexPattern = Pattern.compile(dateRegex);
                            Matcher priceMatcher = dateRegexPattern.matcher(body);
                            String matchedprice;
                            if (priceMatcher.find()) {

                                if (priceMatcher.group().contains(",")) {

                                    matchedprice = priceMatcher.group().replace(",", "");

                                } else {
                                    matchedprice = priceMatcher.group();
                                }
                                boolean a = new ApplicationUtil().isDouble(matchedprice.substring(3, matchedprice.length()));

                                if (a) {
                                    price = priceMatcher.group().toString().substring(3, priceMatcher.group().toString().length());
                                }
                            }
                        } else {

                            String dateRegex = "([\\d,\\,]*\\.\\d{2})";
                            Pattern dateRegexPattern = Pattern.compile(dateRegex);
                            Matcher priceMatcher = dateRegexPattern.matcher(body);

                            if (priceMatcher.find()) {

                                boolean a = new ApplicationUtil().isDouble(priceMatcher.group().toString());
                                if (a) {
                                    price = priceMatcher.group().toString();
                                }
                            }
                        }
                        if (price != null) {
                            boolean status = false;
                            for (int k = 0; k < dateRegexArray.length; k++) {
                                dateRegex = dateRegexArray[k];
                                datePattern = Pattern.compile(dateRegex);
                                dateMatcher = datePattern.matcher(body);

                                if (dateMatcher.find()) {
                                    status = true;
                                    break;
                                }
                            }
                            if (status) {
                                if (body.contains("credit")) {

                                    db.addMessageDetails(new MessageDetail(address, price, "NA", "NA", dateMatcher.group()));
                                } else if (body.contains("debit")) {
                                    // DBDataSource db = new DBDataSource(getActivity());
                                    db.addMessageDetails(new MessageDetail(address, "NA", price, "NA", dateMatcher.group()));
                                } else {
                                    db.addMessageDetails(new MessageDetail(address, "NA", "NA", price, dateMatcher.group()));
                                }

                            } else {
                                if (body.contains("credit")) {
                                    // DBDataSource db = new DBDataSource(getActivity());
                                    db.addMessageDetails(new MessageDetail(address, price, "NA", "NA", "NA"));
                                } else if (body.contains("debit")) {
                                    //  DBDataSource db = new DBDataSource(getActivity());
                                    db.addMessageDetails(new MessageDetail(address, "NA", price, "NA", "NA"));
                                } else {
                                    db.addMessageDetails(new MessageDetail(address, "NA", "NA", price, "NA"));
                                }
                            }

                        } else {
                            // DBDataSource db = new DBDataSource(getActivity());
                            db.addMessageDetails(new MessageDetail(address, "NA", "NA", "NA", "NA"));

                        }
                        break;
                    }
                }
            }
            db.close();
            c.close();

            return "";
        }

        protected void onPostExecute(String result) {
            List<MessageDetail> msgDetail = db.getAllMessageDetails();
            if (msgDetail.size() > 0) {

                alertText.setVisibility(View.GONE);
                alertText.clearAnimation();
                adapter = new MessagelistAdapter(getActivity(), R.layout.msglistrow, msgDetail);
                progressbar.setVisibility(View.GONE);
                msgList.setAdapter(adapter);

            } else {

                progressbar.setVisibility(View.GONE);
                alertText.setText("No relevant messages available !");

            }
            db.close();
        }
    }

}
