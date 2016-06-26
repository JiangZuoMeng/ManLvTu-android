package com.jiangzuomeng.fleetingtime.viewConctroller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jiangzuomeng.fleetingtime.R;
import com.jiangzuomeng.fleetingtime.network.NetworkJsonKeyDefine;

public class MainActivity extends AppCompatActivity implements FragmentIndicator.OnIndicateListener {
    public static String W = "wilbert";
    private int mark = 0;
    public static Fragment[] mFragments;
    FragmentIndicator mIndicator;
    public static  int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = getIntent().getIntExtra(NetworkJsonKeyDefine.ID, -1);
        Log.d(W, userId + "");

        setFragmentIndicator(0);
    }


    private void setFragmentIndicator(int whichIsDefault) {
        mark = whichIsDefault;
        mFragments = new Fragment[3];
        mFragments[0] = getSupportFragmentManager().findFragmentById(R.id.nearby_fragment);
        mFragments[1] = getSupportFragmentManager().findFragmentById(R.id.album_fragment);
        mFragments[2] = getSupportFragmentManager().findFragmentById(R.id.circle_fragment);

        switchFragment(whichIsDefault, false);

        mIndicator=(FragmentIndicator) findViewById(R.id.indicator);

        mIndicator.setOnIndicateListener(this);
    }

    @Override
    public void OnIndicate(View v, int which) {
        switchFragment(which, false);
    }


    private void switchFragment(int which, boolean animate) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (animate) {
            if (which > mark)
                ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
            else
                ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        for (int i = 0; i < mFragments.length; ++i)
            ft.hide(mFragments[i]);
        ft.show(mFragments[which]);
        ft.commit();
        /*if (mFragments[which] instanceof CallLogFragment)
            mFragments[which].onResume();*/
        FragmentIndicator.setIndicator(which);
        mark = which;
    }

}
