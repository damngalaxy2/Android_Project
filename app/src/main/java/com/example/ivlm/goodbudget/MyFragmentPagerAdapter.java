package com.example.ivlm.goodbudget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by _IVLM on 3/15/2016.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> FragmentList) {
        super(fm);
        this.fragmentList = FragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
