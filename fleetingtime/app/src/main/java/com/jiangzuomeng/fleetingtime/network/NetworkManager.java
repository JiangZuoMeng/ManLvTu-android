package com.jiangzuomeng.fleetingtime.network;

import android.content.Context;

import com.amap.api.maps2d.model.LatLng;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jiangzuomeng.fleetingtime.models.Travel;
import com.jiangzuomeng.fleetingtime.models.TravelItem;
import com.jiangzuomeng.fleetingtime.models.User;

import java.net.MalformedURLException;

/**
 * Created by wilbert on 2016/6/27.
 */
public class NetworkManager {
    private static NetworkManager networkManager;
    private Context mContext;
    private VolleyManager volleyManager;

    public interface INetworkResponse {
        void doResponse(String response);
    }

    private NetworkManager(Context context) {
        mContext = context.getApplicationContext();
        volleyManager = VolleyManager.getInstance(context);
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if (networkManager == null) {
            networkManager = new NetworkManager(context);
        }
        return networkManager;
    }

    private void methodGet(String url, FunctionResponseListener listener,
                           Response.ErrorListener errorListener) {
        StringRequest registerRequest = new StringRequest(Request.Method.GET,
                url, listener, errorListener);
        volleyManager.addToRequestQueue(registerRequest);
    }

    public void registerUser(User user, FunctionResponseListener listener,
                             Response.ErrorListener errorListener) throws MalformedURLException {
        methodGet(user.getAddUrl().toString(), listener, errorListener);
    }

    public void login(User user, FunctionResponseListener listener,
                      Response.ErrorListener errorListener) throws MalformedURLException {
        methodGet(user.getLoginUrl().toString(), listener, errorListener);
    }

    public void queryNearbyTravelItem(LatLng currentLocation, FunctionResponseListener listener,
                                      Response.ErrorListener errorListener)
            throws MalformedURLException {
        Double distance = NetworkJsonKeyDefine.NEAR_DISTANCE;
        double locationLat = currentLocation.latitude;
        double locationLng = currentLocation.longitude;
        String url = TravelItem.getQueryNearbyUrl(locationLat - distance,
                locationLat + distance, locationLng - distance,
                locationLng + distance).toString();
        methodGet(url, listener, errorListener);
    }

    public void queryTravelIdListByUserId(int userId, FunctionResponseListener listener,
                                          Response.ErrorListener errorListener)
            throws MalformedURLException {
        Travel travel = new Travel();
        travel.userId = userId;
        String url = travel.getQueryAllUrl().toString();
        methodGet(url, listener, errorListener);
    }

    public void queryTravelItemIdListByTravelId(int travelId, FunctionResponseListener listener,
                                                Response.ErrorListener errorListener) throws MalformedURLException {
        TravelItem travelItem = new TravelItem();
        travelItem.travelId = travelId;
        String url = null;
        url = travelItem.getQueryAllUrl().toString();
        methodGet(url, listener, errorListener);
    }

    public void addNewTravel(Travel travel, FunctionResponseListener listener,
                             Response.ErrorListener errorListener) throws MalformedURLException {
        String url = travel.getAddUrl().toString();
        methodGet(url, listener, errorListener);
    }

    public void queryTravelByTravelId(int travelId, FunctionResponseListener listener,
                                      Response.ErrorListener errorListener) throws MalformedURLException {
        Travel travel = new Travel();
        travel.id = travelId;
        methodGet(travel.getQueryUrl().toString(), listener, errorListener);
    }

    public void removeTravelByTravelId(int travelId, FunctionResponseListener listener,
                                       Response.ErrorListener errorListener) throws MalformedURLException {
        Travel travel = new Travel();
        travel.id = travelId;
        methodGet(travel.getRemoveUrl().toString(), listener, errorListener);
    }

    public void updateTravel(Travel travel, FunctionResponseListener listener,
                             Response.ErrorListener errorListener) throws MalformedURLException {
        methodGet(travel.getUpdateUrl().toString(), listener, errorListener);
    }

    public void addNewTravelItem(TravelItem travelItem, FunctionResponseListener listener,
                                 Response.ErrorListener errorListener) throws MalformedURLException {
        methodGet(travelItem.getAddUrl().toString(), listener, errorListener);
    }

    public void queryTravelItemByTravelItemId(int travelItemId, FunctionResponseListener listener,
                                              Response.ErrorListener errorListener) throws MalformedURLException {
        TravelItem travelItem = new TravelItem();
        travelItem.id = travelItemId;
        methodGet(travelItem.getQueryUrl().toString(), listener, errorListener);
    }

    public void removeTravelItemByTravelItemId(int travelItemId, FunctionResponseListener listener,
                                               Response.ErrorListener errorListener) throws MalformedURLException {
        TravelItem travelItem = new TravelItem();
        travelItem.id = travelItemId;
        methodGet(travelItem.getRemoveUrl().toString(), listener, errorListener);
    }

    public void updateTravelItem(TravelItem travelItem, FunctionResponseListener listener,
                                 Response.ErrorListener errorListener) throws MalformedURLException {
        methodGet(travelItem.getUpdateUrl().toString(), listener, errorListener);
    }
}