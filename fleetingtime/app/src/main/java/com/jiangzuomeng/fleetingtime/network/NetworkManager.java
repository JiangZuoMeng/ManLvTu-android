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

    public void registerUser(User user, FunctionResponseListener listener,
                             Response.ErrorListener errorListener) throws MalformedURLException {
        StringRequest registerRequest = new StringRequest(Request.Method.GET,
                user.getAddUrl().toString(), listener, errorListener);
        volleyManager.addToRequestQueue(registerRequest);
    }

    public void login(User user, FunctionResponseListener listener,
                      Response.ErrorListener errorListener) throws MalformedURLException {
        StringRequest loginRequest = new StringRequest(Request.Method.GET,
                user.getLoginUrl().toString(),
                listener, errorListener);
        volleyManager.addToRequestQueue(loginRequest);
    }

    public void queryNearbyTravelItem(LatLng currentLocation, FunctionResponseListener listener,
                                      Response.ErrorListener errorListener) throws MalformedURLException {
        Double distance = NetworkJsonKeyDefine.NEAR_DISTANCE;
        double locationLat = currentLocation.latitude;
        double locationLng = currentLocation.longitude;
        String url = TravelItem.getQueryNearbyUrl(locationLat - distance,
                locationLat + distance, locationLng - distance,
                locationLng + distance).toString();
        StringRequest request = new StringRequest(Request.Method.GET,
                url, listener, errorListener);
        volleyManager.addToRequestQueue(request);
    }

    public void queryTravelIdListByUserId(int userId, FunctionResponseListener listener,
                                          Response.ErrorListener errorListener) throws MalformedURLException {
        Travel travel = new Travel();
        travel.userId = userId;
        String url = travel.getQueryAllUrl().toString();
        StringRequest request = new StringRequest(Request.Method.GET,
                url, listener, errorListener);
        volleyManager.addToRequestQueue(request);
    }

    public void queryTravelItemIdListByTravelId(int travelId, FunctionResponseListener listener,
                                                Response.ErrorListener errorListener) throws MalformedURLException {
        TravelItem travelItem = new TravelItem();
        travelItem.travelId = travelId;
        String url = null;
        url = travelItem.getQueryAllUrl().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, listener, errorListener);
        volleyManager.addToRequestQueue(stringRequest);
    }

}