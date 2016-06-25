package com.jiangzuomeng.fleetingtime.viewConctroller;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.jiangzuomeng.fleetingtime.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragment extends Fragment implements LocationSource,
        AMapLocationListener  {

    private View view;

    private static final String TAG = "NEARBY";

    //tab
    private TabLayout tabLayout;
    private List<String> titleList;
    private com.getbase.floatingactionbutton.FloatingActionButton startBtn;
    private com.getbase.floatingactionbutton.FloatingActionButton captureBtn;
    private com.getbase.floatingactionbutton.FloatingActionButton finishBtn;

    private MapView mMapView = null;
    private AMap aMap = null;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    public NearbyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_nearby, container, false);

        mMapView = (MapView)view.findViewById(R.id.mapView_nearBy);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);

        aMap = mMapView.getMap();
        initMap();

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

    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

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
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity().getApplicationContext());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }

    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
}
