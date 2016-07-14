package com.jiangzuomeng.travelmap;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.List;

/**
 * Created by wilbert on 2015/10/27.
 */
public class CollectionPagerAdapter  extends FragmentPagerAdapter {
    private int numberOfType;

    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public CollectionPagerAdapter(FragmentManager fm, int num) {
        super(fm);
        numberOfType = num;
    }

    @Override
    public Fragment getItem(int position) {
        //return fragements.get(position);
        if (position == 0)
        return new AMapFragment();
        else
            return  new AMap_MySelf_Fragment();
    }

    @Override
    public int getCount() {
        return numberOfType;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1) {
            return "我的旅图";
        }
        return "Position " + position;
    }
}
