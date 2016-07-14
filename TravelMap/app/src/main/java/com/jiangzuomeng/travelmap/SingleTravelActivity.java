package com.jiangzuomeng.travelmap;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.jiangzuomeng.Adapter.SingleTravelItemListViewAdapter;
import com.jiangzuomeng.dataManager.DataManager;
import com.jiangzuomeng.dataManager.NetworkConnectActivity;
import com.jiangzuomeng.dataManager.NetworkHandler;
import com.jiangzuomeng.modals.TravelItem;
import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SingleTravelActivity
        extends AppCompatActivity
        implements AMap.OnMapClickListener, AdapterView.OnItemLongClickListener
        ,AdapterView.OnItemClickListener, AMap.OnMarkerDragListener
        ,NetworkConnectActivity, View.OnClickListener {

    public static final String INTENT_TRAVEL_KEY = "travelId";
    public static final String INTENT_TRAVEL_ITEM_KEY = "travelItemId";
    private MapView mapView;
    private AMap aMap;
    private Polyline polyline;
    private ArrayList<Marker> markers = new ArrayList<>();
    List<TravelItem> travelItemList = new ArrayList<>();
    private boolean isMapMovable = true;
    private ListView singleTravelItemsListView;
    PopupWindow popupWindow;
    private int listViewClickPosition = 0;
    private int currentTravelId;
    SingleTravelItemListViewAdapter singleTravelItemAdapter;
    private boolean initial = true;
    android.support.v7.app.ActionBar supportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_travel);

        // setup single travel activity list view
        singleTravelItemsListView = (ListView)findViewById(R.id.SingleTravelMapListView);
        singleTravelItemsListView.setLongClickable(true);
        singleTravelItemsListView.setOnItemLongClickListener(this);
        singleTravelItemsListView.setOnItemClickListener(this);
        singleTravelItemAdapter = new SingleTravelItemListViewAdapter(this, this);


        initPopupWindow();

        // setup map
        mapView = (MapView) findViewById(R.id.SingleTravelMapMapView);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        setupMap();

        // setting action bar
        supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

        currentTravelId = getIntent().getIntExtra(INTENT_TRAVEL_KEY, -1);
        DataManager.getInstance(getApplicationContext()).queryTravelByTravelId(currentTravelId, new NetworkHandler(this));
        DataManager.getInstance(getApplicationContext()).queryTravelItemIdListByTravelId(currentTravelId, new NetworkHandler(this));
    }
    private void initData() {
        singleTravelItemAdapter.setup(travelItemList);
        singleTravelItemsListView.setAdapter(singleTravelItemAdapter);

        aMap.clear();
        markers.clear();

        // tend to move camera to location of first item
        if (initial && !travelItemList.isEmpty()) {
            initial = false;
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(
                    new LatLng(travelItemList.get(0).locationLat, travelItemList.get(0).locationLng)));
        }

        for (TravelItem travelItem : travelItemList) {
            LatLng latLng = new LatLng(travelItem.locationLat, travelItem.locationLng);
            addMarker(new MarkerOptions().position(latLng).draggable(true));
        }
        linkMarkersOfMap();
    }



    private void setupMap() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerDragListener(this);
    }

    private void initPopupWindow() {
        View popViewContent = getLayoutInflater().inflate(R.layout.popup_window_for_single_travel_view_list_item, null);

        Button editButton = (Button) popViewContent.findViewById(R.id.SingleTravelActivityListViewPopupEditButton);
        Button deleteButton = (Button) popViewContent.findViewById(R.id.SingleTravelActivityListViewPopupDeleteButton);

        editButton.setOnClickListener(this);

        deleteButton.setOnClickListener(this);

        popupWindow = new PopupWindow(popViewContent, ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
    }

    private void addMarker(MarkerOptions markerOptions) {
        markers.add(aMap.addMarker(markerOptions));
    }

    private void linkMarkersOfMap() {
        if (polyline != null) {
            polyline.remove();
        }

        PolylineOptions polylineOptions = new PolylineOptions()
                .color(getResources().getColor(R.color.single_travel_polyline_color));

        for (Marker marker: markers) {
            polylineOptions.add(marker.getPosition());
        }

        polyline = aMap.addPolyline(polylineOptions);
    }
    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        DataManager.getInstance(getApplicationContext())
                .queryTravelItemIdListByTravelId(currentTravelId, new NetworkHandler(this));
    }
    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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
    public void onMapClick(LatLng latLng) {
        //addMarker(new MarkerOptions().position(latLng));
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        listViewClickPosition = position;
        popupWindow.showAsDropDown(view, view.getWidth() / 2, -view.getHeight() / 2);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add_new_position:
                /*addMarker(new MarkerOptions()
                                .position(aMap.getCameraPosition().target)
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                );*/
                TravelItem travelItem = new TravelItem();
                travelItem.travelId = currentTravelId;
                travelItem.text = getResources().getString(R.string.single_travel_default_list_view_item_description);
                travelItem.locationLng = aMap.getCameraPosition().target.longitude;
                travelItem.locationLat = aMap.getCameraPosition().target.latitude;
                DataManager.getInstance(getApplicationContext()).addNewTravelItem(travelItem, new NetworkHandler(this));
                break;
            case R.id.action_lock_map:
                isMapMovable = !isMapMovable;
                aMap.getUiSettings().setScrollGesturesEnabled(isMapMovable);
                if (isMapMovable) {
                    item.setIcon(R.drawable.ic_lock_open_white_24dp);
                } else {
                    item.setIcon(R.drawable.ic_lock_outline_white_24dp);
                }
        }
        return true;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AlbumViewerActivity.class);
        intent.putExtra(INTENT_TRAVEL_ITEM_KEY, travelItemList.get(position).id);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_travel_activity_actionbar_menu, menu);
        return true;
    }
    @Override
    public void onMarkerDragStart(Marker marker) {

    }
    @Override
    public void onMarkerDrag(Marker marker) {
        linkMarkersOfMap();
    }
    @Override
    public void onMarkerDragEnd(Marker marker) {
        TravelItem targetTravelItem = travelItemList.get(markers.indexOf(marker));

        targetTravelItem.locationLat = marker.getPosition().latitude;
        targetTravelItem.locationLng = marker.getPosition().longitude;

        DataManager.getInstance(getApplicationContext()).updateTravelItem(targetTravelItem, new NetworkHandler(this));
    }

    @Override
    public void handleNetworkEvent(String status, String request, String target, JSONObject originJSONObject) throws JSONException {
        switch (target) {
            case NetworkJsonKeyDefine.TRAVEL_ITEM:
                switch (status) {
                    case NetworkJsonKeyDefine.RESULT_SUCCESS:
                        switch (request) {
                            case NetworkJsonKeyDefine.QUERY_ALL:
                                travelItemList.clear();
                                JSONArray data = originJSONObject.getJSONArray(NetworkJsonKeyDefine.DATA_KEY);
                                for (int count = 0; count < data.length(); count++) {
                                    travelItemList.add(TravelItem.fromJson(
                                            data.getJSONObject(count).toString(), true
                                    ));
                                }
                                initData();
                                break;
                            default:
                                DataManager.getInstance(getApplicationContext()).queryTravelItemIdListByTravelId(currentTravelId, new NetworkHandler(this));
                                break;
                        }
                        break;
                }
                break;
            case NetworkJsonKeyDefine.TRAVEL:
                switch (status) {
                    case NetworkJsonKeyDefine.RESULT_SUCCESS:
                        switch (request) {
                            case NetworkJsonKeyDefine.QUERY:
                                supportActionBar.setTitle(
                                        originJSONObject.getJSONObject(NetworkJsonKeyDefine.DATA_KEY)
                                                .getString(NetworkJsonKeyDefine.NAME)
                                );
                        }
                        break;
                }
                break;
            case NetworkJsonKeyDefine.FILE:
                switch (status) {
                    case NetworkJsonKeyDefine.RESULT_SUCCESS:
                        DataManager.getInstance(getApplicationContext()).queryTravelItemIdListByTravelId(currentTravelId, new NetworkHandler(this));
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SingleTravelActivityListViewPopupDeleteButton:
                popupWindow.dismiss();

                TravelItem targetTravelItem = travelItemList.get(listViewClickPosition);
                DataManager.getInstance(getApplicationContext()).removeTravelItemByTravelItemId(targetTravelItem.id,
                        new NetworkHandler(this));
                break;

            case R.id.SingleTravelActivityListViewPopupEditButton:
                popupWindow.dismiss();

                Intent intent = new Intent(getApplicationContext(), CreateNewItemActivity.class);
                intent.putExtra(INTENT_TRAVEL_KEY, travelItemList.get(listViewClickPosition).id);
                startActivity(intent);
                break;
        }
    }
}
