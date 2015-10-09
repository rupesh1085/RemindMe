package com.bluepi.remindme.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bluepi.remindme.R;

import java.util.ArrayList;

/**
 * Created by rupesh on 17/9/15.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private ArrayList<Object> childtems;
    private LayoutInflater inflater;
    private ArrayList<String> parentItems, child;

    public ExpandableAdapter(ArrayList<String> parents, ArrayList<Object> childern) {
        this.parentItems = parents;
        this.childtems = childern;
    }

    public void setInflater(LayoutInflater inflater, Activity activity) {
        this.inflater = inflater;
        this.activity = activity;
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((ArrayList<String>) childtems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.expandable_group_list, null);
        }
        textView = (TextView) convertView.findViewById(R.id.text);
        textView.setText(parentItems.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        child = (ArrayList<String>) childtems.get(groupPosition);
        TextView textView = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.expandable_child_list, null);
        }
        textView = (TextView) convertView.findViewById(R.id.text);
        textView.setText(child.get(childPosition));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, child.get(childPosition),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
