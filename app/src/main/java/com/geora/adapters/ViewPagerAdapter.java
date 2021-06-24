package com.geora.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.util.SparseArray;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private String[] title;
    private SparseArray<Fragment> fragments;

    public ViewPagerAdapter(Context context, FragmentManager fm, SparseArray<Fragment> fragments, String[] title) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position+1);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }


}
