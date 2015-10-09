package com.bluepi.remindme.fragment;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.bluepi.remindme.R;
import com.bluepi.remindme.adapter.ExpandableAdapter;
import com.bluepi.remindme.datasource.DBDataSource;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rupesh on 12/7/15.
 */
public class SummaryFragment extends Fragment {

    private View rootView;
    private ExpandableListView expense_list ;
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    private ExpandableAdapter adapter;
    private String[] mListGroupTitles;
    private File database ;
    private DBDataSource dbSource ;
    private LinkedHashMap<String , String> graphDataMap = new LinkedHashMap<>();
    private BarChart chart ;
    private BarData data ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.summary_main, container, false);

        database= getActivity().getApplicationContext().getDatabasePath("Remindme");
        dbSource = new DBDataSource(getActivity());

        new AsyncTaskRunner().execute();
        expense_list = (ExpandableListView) rootView.findViewById(R.id.list);
        mListGroupTitles = getActivity().getResources().getStringArray(R.array.expandable_list_group_item);
        setGroupParents();
        setChildData();
        adapter = new ExpandableAdapter(parentItems, childItems);
        adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
        expense_list.setAdapter(adapter);


        chart = (BarChart) rootView.findViewById(R.id.chart);
        chart.setGridBackgroundColor(Color.LTGRAY);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.DKGRAY);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);



        return rootView;

    }

    public void setGroupParents() {

        for (int i = 0; i < mListGroupTitles.length ; i++) {
            parentItems.add(mListGroupTitles[i]);
        }
    }

    public void setChildData() {

        if(database.exists()){
            // Credit
            ArrayList<String> child = new ArrayList<String>();
            for (int i = 0; i < dbSource.getAllMessageDetails().size(); i++) {
                String credit_value = dbSource.getAllMessageDetails().get(i).getCredit_amount();
                if(!credit_value.equalsIgnoreCase("NA")){

                    child.add(credit_value);
                }
                if(child.size() == 3){
                }
                break;
            }
            childItems.add(child);
            // Spends
            child = new ArrayList<String>();
            for (int i = 0; i < dbSource.getAllMessageDetails().size(); i++) {
                String spend_value = dbSource.getAllMessageDetails().get(i).getSpend_amount();
                if(!spend_value.equalsIgnoreCase("NA")){

                    child.add(spend_value);
                }
                if(child.size() == 5){
                    break;
                }
            }
            childItems.add(child);
            // Debit
            child = new ArrayList<String>();
            for (int i = 0; i < dbSource.getAllMessageDetails().size(); i++) {
                String debit_value = dbSource.getAllMessageDetails().get(i).getDebit_amount();
                if(!debit_value.equalsIgnoreCase("NA")){

                    child.add(debit_value);
                }
                if(child.size() == 5){
                    break;
                }
            }
            childItems.add(child);
        }else{
            // Credit
            ArrayList<String> child = new ArrayList<String>();
            childItems.add(child);
            // Spends
            child = new ArrayList<String>();
            childItems.add(child);
            // Debit
            child = new ArrayList<String>();
            childItems.add(child);
        }
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
        ArrayList<Float> spend = new ArrayList<>();

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        for(Map.Entry m : graphDataMap.entrySet()){

            spend.add(Float.parseFloat(String.valueOf(m.getValue())));
        }

        for (int i = 0; i < spend.size() ; i++) {
            BarEntry a = new BarEntry(spend.get(i) , i );
            valueSet1.add(a);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Monthly expenses");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        for(Map.Entry m : graphDataMap.entrySet()){

            xAxis.add((String.valueOf(m.getKey())));
        }
        return xAxis;
    }


    /**
     * @author Rupesh
     * Private class which runs the long operation. ( Sleeping for some time )
     */
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {

            graphDataMap = dbSource.getGraphData();

            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            data = new BarData(getXAxisValues(), getDataSet());
            chart.setData(data);
            chart.setDescription("");
            chart.animateXY(4000, 2000);
            chart.invalidate();
            chart.setVisibility(View.VISIBLE);

        }
    }

}
