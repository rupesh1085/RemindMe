package com.bluepi.remindme;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bluepi.remindme.fragment.MessageFragment;
import com.bluepi.remindme.fragment.SummaryFragment;

/**
 * Created by rupesh on 15/9/15.
 */
public class HomeActivity extends Activity {

    private String[] mListTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle ;
    private ImageView navdrawer ;
    private TextView titleText ;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        mListTitles = getResources().getStringArray(R.array.drawer_list_item);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        navdrawer = (ImageView) findViewById(R.id.navdraw);
        titleText = (TextView) findViewById(R.id.action_text);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_selectable_list_item, mListTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        navdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    return;
                }else{

                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

        switch(position){

            case 0 :
                Fragment summary_frag = new SummaryFragment() ;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft.replace(R.id.content_frame, summary_frag, "fragment");
                ft.commit();


                break ;
            case 1 :

              //  startActivity(new Intent(getApplicationContext() , MainActivity.class));

                Fragment msg_frag = new MessageFragment() ;
                FragmentTransaction ft_msg = getFragmentManager().beginTransaction();
                ft_msg.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft_msg.replace(R.id.content_frame, msg_frag, "fragment");
                ft_msg.commit();

                break ;
            case 2 :

                break ;
            case 3 :

                break ;
            default :
                break ;
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mListTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        titleText.setText(mTitle);
    }

}
