package com.jiangzuomeng.fleetingtime.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by wilbert on 2016/6/26.
 */
public class VolleyManager {
    private static VolleyManager volleyManager;
    private Context mContext;
    private RequestQueue mRequestQueue;

    private VolleyManager(Context context) {
        mContext = context;
    }

    public static synchronized VolleyManager getInstance(Context context) {
        if (volleyManager == null) {
            volleyManager = new VolleyManager(context);
        }
        return volleyManager;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static JSONObject getJsonObject(String response) {
        JSONTokener parser = new JSONTokener(response);
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
