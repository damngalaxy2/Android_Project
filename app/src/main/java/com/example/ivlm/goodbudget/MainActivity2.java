package com.example.ivlm.goodbudget;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity2 extends Fragment implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener{
    TabHost tabHost;
    DatabaseHandler handler;
    ViewPager viewPager;
    MyFragmentPagerAdapter myFragmentPagerAdapter;

    //-----------------Self Variable--------------------
    ImageButton FAB;

    //----------Envelopes Variable----------------------

    String[] tabnames = {"Envelopes","Transactions","Accounts","Reports"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main2, container, false);
        handler = new DatabaseHandler(getActivity());
        //--------------Declare Variable--------------
        FAB = (ImageButton) v.findViewById(R.id.FAB);
        this.initViewPager(v);

        //--------Set on Click Listener---------------
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddTransaction.class));
            }
        });

        //----------------Tabhost---------------------
        tabHost = (TabHost) v.findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.getTabWidget().setStripEnabled(false);

        for(int i =0;i<tabnames.length;i++){
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabnames[i]);
            tabSpec.setIndicator(tabnames[i]);
            tabSpec.setContent(new FakeContent(getActivity()));
            tabHost.addTab(tabSpec);
            tabHost.setOnTabChangedListener(this);
        }

        tabHost.setCurrentTab(0);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#2bbf8b")); //unselected
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#DEDEDE"));
        }
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#2bbf8b")); //selected
        TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#ffffff"));

        return v;
    }

    public void initViewPager(View v){
        viewPager = (ViewPager) v.findViewById(R.id.view_pager);
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(new Envelopes_Fragment());
        list.add(new Transactions_Fragment());
        list.add(new Accounts_Fragment());
        list.add(new Reports_Fragment());

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), list);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String tabId) {
        int selectedPage = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedPage);
        if(selectedPage < 3){
            FAB.setVisibility(View.VISIBLE);
        }else if(selectedPage == 3){
            FAB.setVisibility(View.GONE);
        }
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#2bbf8b")); //unselected
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#DEDEDE"));
        }
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#2bbf8b")); //selected
        TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#ffffff"));
    }

    public class FakeContent implements TabHost.TabContentFactory{
        Context context;
        public FakeContent(Context mContext){context = mContext;}
        @Override
        public View createTabContent(String tag) {
            View fakeview = new View(context);
            fakeview.setMinimumWidth(0);
            fakeview.setMinimumHeight(0);

            return fakeview;
        }
    }
}
