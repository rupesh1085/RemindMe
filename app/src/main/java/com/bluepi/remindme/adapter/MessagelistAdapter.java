package com.bluepi.remindme.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bluepi.remindme.R;
import com.bluepi.remindme.model.MessageDetail;
import java.util.List;

/**
 * Created by rupesh on 15/9/15.
 */
public class MessagelistAdapter extends ArrayAdapter<MessageDetail> {



    public MessagelistAdapter(Context context, int resource, List<MessageDetail> objects) {
        super(context, resource, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.msglistrow,null);
        TextView msgSender = (TextView)convertView.findViewById(R.id.sendertext);
        TextView dueDate = (TextView)convertView.findViewById(R.id.duedatetext);
        TextView dueAmount = (TextView)convertView.findViewById(R.id.dueamounttext);

        ToggleButton toggle = (ToggleButton) convertView.findViewById(R.id.toggle) ;

        MessageDetail msgDetail = getItem(position);
        if(msgDetail != null){

            msgSender.setText(msgDetail.getSender_name());
            if(!msgDetail.getCredit_amount().equalsIgnoreCase("NA")){
                dueAmount.setText(msgDetail.getCredit_amount());
            }else if(!msgDetail.getDebit_amount().equalsIgnoreCase("NA")){
                dueAmount.setText(msgDetail.getDebit_amount());
            }else if(!msgDetail.getSpend_amount().equalsIgnoreCase("NA")){
                dueAmount.setText(msgDetail.getSpend_amount());
            }else{
                //dueAmount.setText("NA");
            }

            if(! msgDetail.getDate().equalsIgnoreCase("NA")){

                dueDate.setText(msgDetail.getDate());
            }
        }

        return convertView;
    }
}
