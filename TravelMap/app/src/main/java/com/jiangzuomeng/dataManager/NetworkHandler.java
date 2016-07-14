package com.jiangzuomeng.dataManager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by ekuri on 15-12-15.
 */
public class NetworkHandler extends Handler {
    NetworkConnectActivity targetActivity;

    public NetworkHandler(NetworkConnectActivity target) {
        targetActivity = target;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what != NetworkJsonKeyDefine.NETWORK_OPERATION) {
            return;
        }

        String resultJsonString = msg.getData().getString(NetworkJsonKeyDefine.NETWORK_RESULT_KEY);
        Log.v("ekuri", "Get json string: " + resultJsonString);
        JSONTokener parser = new JSONTokener(resultJsonString);

        try {
            JSONObject jsonObject = (JSONObject) parser.nextValue();
            targetActivity.handleNetworkEvent(jsonObject.getString(NetworkJsonKeyDefine.RESULT_KEY),
                    jsonObject.getString(NetworkJsonKeyDefine.REQUEST_KEY),
                    jsonObject.getString(NetworkJsonKeyDefine.TARGET_KEY),
                    jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.handleMessage(msg);
    }
}
