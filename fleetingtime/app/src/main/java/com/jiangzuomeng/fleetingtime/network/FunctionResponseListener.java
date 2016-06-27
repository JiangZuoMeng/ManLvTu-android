package com.jiangzuomeng.fleetingtime.network;

import android.util.Log;

import com.android.volley.Response;

/**
 * Created by wilbert on 2016/6/27.
 */
public class FunctionResponseListener implements Response.Listener<String> {
    NetworkManager.INetworkResponse iNetworkResponse;
    public FunctionResponseListener(NetworkManager.INetworkResponse iNetworkResponse) {
        this.iNetworkResponse = iNetworkResponse;
    }

    @Override
    public void onResponse(String response) {
        Log.d("wilbert", "解密ing");
        iNetworkResponse.doResponse(response);
    }
}
