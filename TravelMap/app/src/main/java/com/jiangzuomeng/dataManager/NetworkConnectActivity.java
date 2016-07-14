package com.jiangzuomeng.dataManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ekuri on 15-12-15.
 */
public interface NetworkConnectActivity {
    void handleNetworkEvent(String result, String request, String target, JSONObject originJSONObject) throws JSONException;
}
