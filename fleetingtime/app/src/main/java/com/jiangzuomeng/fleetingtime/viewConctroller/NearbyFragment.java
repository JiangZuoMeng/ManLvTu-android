package com.jiangzuomeng.fleetingtime.viewConctroller;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.jiangzuomeng.fleetingtime.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragment extends Fragment {

    private View view;

    private static final String TAG = "NEARBY";


    //tab
    private TabLayout tabLayout;
    private List<String> titleList;
    private com.getbase.floatingactionbutton.FloatingActionButton startBtn;
    private com.getbase.floatingactionbutton.FloatingActionButton captureBtn;
    private com.getbase.floatingactionbutton.FloatingActionButton finishBtn;



    //map
//    private MapView mMapView = null;
//    private AMap aMap;

    public NearbyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_nearby, container, false);



//        mMapView = (MapView)view.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
//        mMapView.onCreate(savedInstanceState);

//        init();

        //tab
        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);

        titleList = new ArrayList<String>();
        titleList.add("美食");
        titleList.add("风景");
        titleList.add("人文");
        titleList.add("城市");

        tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式

        for (String title : titleList) {
            tabLayout.addTab(tabLayout.newTab().setText(title));

        }

        tabLayout.setOnTabSelectedListener(tabListener);



        //Btn
        startBtn =(com.getbase.floatingactionbutton.FloatingActionButton)view.findViewById(R.id.menu_start);
        captureBtn =(com.getbase.floatingactionbutton.FloatingActionButton)view.findViewById(R.id.menu_capture);
        finishBtn =(com.getbase.floatingactionbutton.FloatingActionButton)view.findViewById(R.id.menu_finish);

        startBtn.setOnClickListener(btnListener);
        captureBtn.setOnClickListener(btnListener);
        finishBtn.setOnClickListener(btnListener);


        return view;
    }




    /**
     * tabListener
     */
    private TabLayout.OnTabSelectedListener tabListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            // TODO: 16/6/24  refresh the map
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };


    /**
     * btnListener
     */
    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.menu_start:
                    System.out.println("start");
                    captureBtn.setVisibility(View.VISIBLE);
                    finishBtn.setVisibility(View.VISIBLE);
                    startBtn.setVisibility(View.GONE);
                    // TODO: 16/6/25 start journey
                    break;

                case R.id.menu_capture:
                    System.out.println("capture");
                    // TODO: 16/6/25 take photos
                    break;


                case R.id.menu_finish:
                    System.out.println("finish");
                    finishBtn.setVisibility(View.GONE);
                    captureBtn.setVisibility(View.GONE);
                    startBtn.setVisibility(View.VISIBLE);

                    // TODO: 16/6/25 finish journey ,edit album

                    break;
            }
        }
    };

    /**
     * 初始化AMap对象
     */
/*    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();

        }

    }*/

    @Override
    public void onHiddenChanged(boolean hidd) {
        if(!hidd) {
            System.out.println(TAG);
            getActivity().setTitle(getResources().getString(R.string.nearby));

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
//        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
//        mMapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
//        mMapView.onSaveInstanceState(outState);
    }
}
