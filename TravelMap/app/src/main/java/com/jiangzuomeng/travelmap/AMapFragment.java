package com.jiangzuomeng.travelmap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.jiangzuomeng.Adapter.ShowPictureAdapter;
import com.jiangzuomeng.dataManager.DataManager;
import com.jiangzuomeng.dataManager.NetworkConnectActivity;
import com.jiangzuomeng.dataManager.NetworkHandler;
import com.jiangzuomeng.modals.TravelItem;
import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wilbert on 2015/10/30.
 */
public class AMapFragment extends Fragment implements LocationSource, AMapLocationListener, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, AMap.OnMapClickListener,
        NetworkConnectActivity{
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy locationManagerProxy;
    private LayoutInflater layoutInflater;
    private Marker current_marker = null;
    private LinearLayout linearLayout;
    private ImageView.OnClickListener popupWindowImageClickListener;
    private DataManager dataManager;
    private NetworkHandler networkHandler;
    private ShowPictureAdapter showPictureAdapter;
    List<TravelItem> nearByTravelItemList = new ArrayList<>();
    HorizontalScrollView horizontalScrollView;
    View popView;
    private LatLng latLng;

    @Override
    public void handleNetworkEvent(String result, String request, String target, JSONObject originJSONObject) throws JSONException {
        if (request.equals(NetworkJsonKeyDefine.QUERY_NEARBY) && target.equals(NetworkJsonKeyDefine.TRAVEL_ITEM)
                && result.equals(NetworkJsonKeyDefine.RESULT_SUCCESS)) {
            nearByTravelItemList.clear();
            JSONArray jsonArray = originJSONObject.getJSONArray(NetworkJsonKeyDefine.DATA_KEY);
            for (int i = 0; i < jsonArray.length(); i++) {
                nearByTravelItemList.add(TravelItem.fromJson(jsonArray.getString(i), true));
            }
            if (!nearByTravelItemList.isEmpty()) {
                dataManager.downLoadFile(nearByTravelItemList.get(0).media, networkHandler);
            }
        }
        if (request.equals(NetworkJsonKeyDefine.FILE_DOWNLOAD)) {
            if (result.equals(NetworkJsonKeyDefine.RESULT_SUCCESS)) {
                try {
                    addNearByImageView(nearByTravelItemList.get(0).media,
                            nearByTravelItemList.get(0).id);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                nearByTravelItemList.remove(0);
                if (!nearByTravelItemList.isEmpty()) {
                    dataManager.downLoadFile(nearByTravelItemList.get(0).media, networkHandler);
                }
            }
        }
    }

    public interface MainActivityListener {
        void notifyLocation(double locationLng, double locationLat);
    }
    MainActivityListener mainActivityListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amap, container, false);
        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        layoutInflater = LayoutInflater.from(getActivity());
        setUpMap();
        InitMyListener();
        dataManager = DataManager.getInstance(getActivity().getApplicationContext());
        networkHandler = new NetworkHandler(this);
        showPictureAdapter = new ShowPictureAdapter(layoutInflater);

        popView = layoutInflater.inflate(R.layout.popup_window_meeting, null);
        linearLayout = (LinearLayout)popView.findViewById(R.id.meeting_linear_layout);

        queryNearbyTimer = new Timer();
        queryNearbyTimer.schedule(queryNearbyTimerTask, 1000, 10000);

        return view;
    }

    private void InitMyListener() {
        popupWindowImageClickListener = new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AlbumViewerActivity.class);
                Log.v("wilbert", "travelItemId: " + v.getId());
                intent.putExtra(SingleTravelActivity.INTENT_TRAVEL_ITEM_KEY, v.getId());
                startActivity(intent);
            }
        };

    }

    private void setUpMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(0.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMapClickListener(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示

        //aMap.getUiSettings().setZoomGesturesEnabled(false);
        //aMap.getUiSettings().setScrollGesturesEnabled(false);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // aMap.setMyLocationType()
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
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
        mListener = null;
        if (locationManagerProxy != null) {
            locationManagerProxy.removeUpdates(this);
            locationManagerProxy.destroy();
        }
        locationManagerProxy = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            //添加marker
            latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

            current_marker = aMap.addMarker(new MarkerOptions().position(latLng).title("my location"));
            mainActivityListener.notifyLocation(aMapLocation.getLongitude(), aMapLocation.getLatitude());
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

    boolean isInbackground = false;
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        isInbackground = false;
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
        isInbackground = true;
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
        queryNearbyTimer.purge();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.v("wilbert", "marker clicked");
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            return  true;
        }
        Log.v("wilbert", "popup show");
        PopupWindow popupWindow = new PopupWindow(popView, ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        Projection projection = aMap.getProjection();
        Point point = projection.toScreenLocation(marker.getPosition());
        popupWindow.showAtLocation(mapView, Gravity.CENTER, 0, 0);
        return true;
        //return false;
    }
    //点击marker之后出现的,由于无法实现滚动效果,所以此部分已经弃用,改为由上面实现popupwindow.
    @Override
    public View getInfoWindow(Marker marker) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.popup_window_meeting, null);
        Log.v("wilbert", "return view");
        horizontalScrollView = (HorizontalScrollView)view.findViewById(R.id.horizontalScrollView);
        linearLayout = (LinearLayout)view.findViewById(R.id.meeting_linear_layout);


        return view;
    }

    private void addNearByImageView(String imageName, int id) throws FileNotFoundException {
        ImageView imageView = new ImageView(getActivity().getApplicationContext());
        Uri uri = DataManager.getUriFromImageName(imageName);
        Bitmap bitmap = dataManager.getBitmapFromUri(uri, 150);
        imageView.setImageBitmap(bitmap);
//        imageView.setPadding(1, 1, 1, 1);
        imageView.setId(id);
        linearLayout.addView(imageView);
        imageView.setOnClickListener(popupWindowImageClickListener);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.v("wilbert", marker.isInfoWindowShown()+" ");
        if (marker.isInfoWindowShown())
            marker.hideInfoWindow();
        Log.v("wilbert", "oninfowindowclick");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //if (current_marker != null && current_marker.isInfoWindowShown())
            current_marker.hideInfoWindow();
        Log.v("wilbert", "map click " + current_marker.isInfoWindowShown());
        //horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
    }

    @Override
    public  void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mainActivityListener = (MainActivityListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnmainActivitylistener");
        }
    }

    Timer queryNearbyTimer;
    Handler queryNearbyTimerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isInbackground)
                return;
            if (msg.what == NetworkJsonKeyDefine.TIMER_OPERATION) {
                startQueryNearBy();
            }
        }
    };
    TimerTask queryNearbyTimerTask = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = NetworkJsonKeyDefine.TIMER_OPERATION;
            queryNearbyTimerHandler.sendMessage(message);
        }
    };

    public void startQueryNearBy() {
        nearByTravelItemList.clear();
        linearLayout.removeAllViews();
        dataManager.queryNearbyTravelItem(latLng, networkHandler);
    }
}
