package com.jiangzuomeng.travelmap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.jiangzuomeng.dataManager.DataManager;
import com.jiangzuomeng.dataManager.NetworkConnectActivity;
import com.jiangzuomeng.dataManager.NetworkHandler;
import com.jiangzuomeng.modals.Travel;
import com.jiangzuomeng.modals.TravelItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;

/**
 * Created by wilbert on 2015/10/30.
 */
public class AMap_MySelf_Fragment extends Fragment implements LocationSource, AMapLocationListener, AMap.OnMarkerClickListener,
        GeocodeSearch.OnGeocodeSearchListener, NetworkConnectActivity {
    public  static final int UPDATE = 35344;
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mlistener;
    private LocationManagerProxy locationManagerProxy;
    private int []imageIds;
    private LayoutInflater layoutInflater;
    private Marker current_marker = null;
    private GeocodeSearch geocoderSearch;
    private List<Marker> markerList;
    double locationLng = 0;
    double locationLat = 0;
    List<Integer> travelIdList = new ArrayList<>();
    HashMap<Marker, Integer> markerIdMap = new HashMap<>();
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE:
                    Bundle bundle = msg.getData();
                    locationLat = bundle.getDouble(MainActivity.LOCATION_LAT_KEY);
                    locationLng = bundle.getDouble(MainActivity.LOCATION_LNG_KEY);
                    initData();
                    setUpMap();
            }
        }
    };

    DataManager dataManager;
    List<Travel> travelList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amap, container, false);
        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        layoutInflater = LayoutInflater.from(getActivity());
        dataManager = DataManager.getInstance(getActivity().getApplicationContext());
        initData();
        setUpMap();
        return view;
    }

    private void initData() {
        markerList = new ArrayList<>();
        imageIds = new int[]{
                R.drawable.test1, R.drawable.test2, R.drawable.test3,
                R.drawable.test4
        };
        dataManager.queryTravelIdListByUserId(MainActivity.userId, new NetworkHandler(this));
    }

    private void setUpMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.ic_bookmark_black_24dp));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setOnMarkerClickListener(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示

        //aMap.getUiSettings().setZoomGesturesEnabled(false);
        //aMap.getUiSettings().setScrollGesturesEnabled(false);
        aMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(4);
        aMap.animateCamera(cameraUpdate);
        CameraUpdate update = CameraUpdateFactory.changeLatLng(new LatLng(30, 104));
        aMap.animateCamera(update);
        // aMap.setMyLocationType()
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mlistener = onLocationChangedListener;
        if (locationManagerProxy == null) {
            locationManagerProxy = LocationManagerProxy.getInstance(getActivity());
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
            locationManagerProxy.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 2000, 10, this);
        }}

    @Override
    public void deactivate() {
        mlistener = null;
        if (locationManagerProxy != null) {
            locationManagerProxy.removeUpdates(this);
            locationManagerProxy.destroy();
        }
        locationManagerProxy = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mlistener != null && aMapLocation != null) {
            mlistener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            //添加marker
            LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            current_marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bookmark_black_24dp))
                    .title("my location"));
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).setHandler(handler);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.v("wilbert", "marker clicked");
        int markerTravelId = markerIdMap.get(marker);
        //// TODO: 2015/11/1 跳转到每一项单独的旅程 返回值true表示默认操作(显示信息窗口)不显示
        Intent intent = new Intent(getActivity(), SingleTravelActivity.class);
        intent.putExtra(SingleTravelActivity.INTENT_TRAVEL_KEY, markerTravelId);
        startActivity(intent);
        return true;
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    //根据地理编码查询结果 回调函数
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (i == 0) {
            LatLonPoint point = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
            markerList.add(aMap.addMarker(new MarkerOptions().position(new LatLng(point.getLatitude(), point.getLongitude()))));
            /*markers.add(aMap.addMarker(new MarkerOptions().position(new LatLng(point.getLatitude(), point.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bookmark_black_24dp))
                    .title("my location")));*/
        }
    }

    @Override
    public void handleNetworkEvent(String result, String request, String target, JSONObject originJSONObject) throws JSONException {
        switch (request) {
            case NetworkJsonKeyDefine.QUERY_ALL:
                switch (target) {
                    case NetworkJsonKeyDefine.TRAVEL:
                        switch (result) {
                            case NetworkJsonKeyDefine.RESULT_SUCCESS:
                                JSONArray jsonArray = originJSONObject.
                                        getJSONArray(NetworkJsonKeyDefine.DATA_KEY);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    travelIdList.add(jsonObject.getInt(NetworkJsonKeyDefine.ID));
                                    dataManager.queryTravelItemIdListByTravelId(travelIdList.get(i),
                                            new NetworkHandler(this));
                                }
                                break;
                        }
                        break;
                    case NetworkJsonKeyDefine.TRAVEL_ITEM:
                        switch (result) {
                            case NetworkJsonKeyDefine.RESULT_SUCCESS:
                                JSONArray jsonArray = originJSONObject.
                                        getJSONArray(NetworkJsonKeyDefine.DATA_KEY);
                                if (jsonArray.length() > 0) {
                                    double locationLng = jsonArray.getJSONObject(0)
                                            .getDouble(NetworkJsonKeyDefine.LOCATION_LNG);
                                    double locationLat = jsonArray.getJSONObject(0)
                                            .getDouble(NetworkJsonKeyDefine.LOCATION_LAT);
                                    int travelId = jsonArray.getJSONObject(0)
                                            .getInt(NetworkJsonKeyDefine.TRAVEL_ID);
                                    LatLng latLng = new LatLng(locationLat, locationLng);
                                    Marker marker = aMap.addMarker(new MarkerOptions().position(latLng));
                                    markerIdMap.put(marker, travelId);
                                } else {
                                    LatLng latLng = new LatLng(locationLat, locationLng);
                                    Marker marker = aMap.addMarker(new MarkerOptions().position(latLng));
                                    markerIdMap.put(marker, MainActivity.currentTravelId);
                                }
                                break;
                        }
                        break;
                }
                break;
        }
    }
}
