package com.jiangzuomeng.fleetingtime.viewConctroller;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jiangzuomeng.fleetingtime.R;
import com.jiangzuomeng.fleetingtime.VO.Album;
import com.jiangzuomeng.fleetingtime.adapter.GalleryAdapter;
import com.jiangzuomeng.fleetingtime.models.Travel;
import com.jiangzuomeng.fleetingtime.models.TravelItem;
import com.jiangzuomeng.fleetingtime.network.FunctionResponseListener;
import com.jiangzuomeng.fleetingtime.network.NetworkJsonKeyDefine;
import com.jiangzuomeng.fleetingtime.network.NetworkManager;
import com.jiangzuomeng.fleetingtime.network.VolleyManager;
import com.jiangzuomeng.fleetingtime.widget.GalleryFlow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {

    private View view;

    private MapView mMapView = null;
    private AMap aMap = null;

    private static final String TAG = "ALBUM";


    //album gallery
    private GalleryFlow galleryFlow;

    private List<Album> albumList = new ArrayList<>();

    private List<Bitmap> tempBitmaps = new ArrayList<>();

    private GalleryAdapter galleryAdapter;

    Bundle save;

    private VolleyManager volleyManager;
    List<Integer> travelIdList = new ArrayList<>();
    HashMap<Marker, Integer> markerIdMap = new HashMap<>();

    private NetworkManager networkManager;

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(getResources().getString(R.string.album));
        System.out.println(getActivity().getTitle());

        view = inflater.inflate(R.layout.fragment_album, container, false);


        //album gallery
        galleryFlow = (GalleryFlow) view.findViewById(R.id.gallery);
        albumList = getAlbumList();
        galleryAdapter = new GalleryAdapter(getContext(), albumList, galleryFlow);

        galleryFlow.setAdapter(galleryAdapter);

        mMapView = (MapView) view.findViewById(R.id.mapView_gallery);
        mMapView.onCreate(savedInstanceState);
//        initMap();

        networkManager = NetworkManager.getInstance(getActivity().getApplicationContext());

        return view;
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(4);
        aMap.animateCamera(cameraUpdate);
        aMap.setOnMarkerClickListener(onMarkerClickListener);

        networkManager = NetworkManager.getInstance(getActivity().getApplicationContext());

        Travel travel = new Travel();
        travel.userId = MainActivity.userId;
        try {
            networkManager.queryTravelIdListByUserId(MainActivity.userId,
                    queryAllListener, queryErrorListener);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    private List<Album> getAlbumList() {
        List<Album> albumList = new ArrayList<>();

        tempBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.beijing));
        tempBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.guangzhou));
        Album album1 = new Album("北京", tempBitmaps.get(0));
        albumList.add(album1);
        Album album2 = new Album("广州", tempBitmaps.get(1));
        albumList.add(album2);
        albumList.add(album1);
        albumList.add(album2);

        return albumList;
    }

    ;

    @Override
    public void onHiddenChanged(boolean hidd) {
        if (!hidd) {
            System.out.println(TAG);
            getActivity().setTitle(getResources().getString(R.string.album));
            initMap();
            mMapView.onResume();
        }
        if (hidd) {
            mMapView.onPause();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        for (Bitmap bit : tempBitmaps) {
            bit.recycle();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    AMap.OnMarkerClickListener onMarkerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            int markerTravelId = markerIdMap.get(marker);
            // TODO: 2016/6/27 跳转到每一个相册详情
            return false;
        }
    };
    private FunctionResponseListener queryAllListener = new FunctionResponseListener(
            new NetworkManager.INetworkResponse() {
                @Override
                public void doResponse(String response) {
                    JSONObject jsonObject = VolleyManager.getJsonObject(response);
                    String result = null;
                    try {
                        result = jsonObject.getString(NetworkJsonKeyDefine.RESULT_KEY);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (result != null && result
                            .equals(NetworkJsonKeyDefine.RESULT_SUCCESS)) {
                        try {
                            JSONArray jsonArray = jsonObject.
                                    getJSONArray(NetworkJsonKeyDefine.DATA_KEY);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject innerObject = jsonArray.getJSONObject(i);
                                travelIdList.add(innerObject.getInt(NetworkJsonKeyDefine.ID));
                                networkManager.queryTravelItemIdListByTravelId(travelIdList.get(i)
                                        , queryItemListener, queryErrorListener);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private Response.ErrorListener queryErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };
    private FunctionResponseListener queryItemListener = new FunctionResponseListener(
            new NetworkManager.INetworkResponse() {
                @Override
                public void doResponse(String response) {

                    JSONObject jsonObject = VolleyManager.getJsonObject(response);
                    String result = null;
                    try {
                        result = jsonObject.getString(NetworkJsonKeyDefine.RESULT_KEY);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (result != null && result
                            .equals(NetworkJsonKeyDefine.RESULT_SUCCESS)) {
                        try {
                            JSONArray jsonArray = jsonObject.
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}
