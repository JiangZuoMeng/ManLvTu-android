package com.jiangzuomeng.modals;

import android.content.ContentValues;
import android.net.Uri;

import com.jiangzuomeng.database.ManLvTuSQLDataType;
import com.jiangzuomeng.networkManager.ManLvTuNetworkDataType;
import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ekuri-PC on 2015/11/21.
 */
public class Comment extends ManLvTuNetworkDataType implements ManLvTuSQLDataType {
    public static final String COMMENT_TABLE_NAME = "comment";
    public int id;
    public int userId;
    public int travelItemId;
    public String text;
    public String time;

    public Comment() {

    }

    @Override
    public ContentValues makeSQLContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", this.userId);
        contentValues.put("travelItemId", this.travelItemId);
        contentValues.put("text", this.text);
        contentValues.put("time", this.time);
        return contentValues;
    }

    public static String makeCreateTableSQLString() {
        return "CREATE TABLE IF NOT EXISTS " + COMMENT_TABLE_NAME +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, travelItemId INTEGER, " +
                "text TEXT, time TEXT)";
    }

    public static Comment fromJson(String json, boolean withId) throws JSONException {
        Comment result = new Comment();
        JSONTokener parser = new JSONTokener(json);
        JSONObject jsonObject = (JSONObject) parser.nextValue();
        if (withId)
            result.id = jsonObject.getInt("id");
        result.travelItemId = jsonObject.getInt("travelItemId");
        result.userId = jsonObject.getInt("userId");
        result.text = jsonObject.getString("text");
        result.time = jsonObject.getString("time");
        return result;
    }

    public String toJson(boolean withId) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        if (withId)
            jsonObject.put("id", id);
        jsonObject.put("travelItemId", this.travelItemId);
        jsonObject.put("userId", this.userId);
        jsonObject.put("text", this.text);
        jsonObject.put("time", this.time);

        return jsonObject.toString();
    }

    @Override
    public URL getAddUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.COMMENT).appendPath(NetworkJsonKeyDefine.ADD)
                .appendQueryParameter(NetworkJsonKeyDefine.TRAVEL_ITEM_ID, Integer.toString(travelItemId))
                .appendQueryParameter(NetworkJsonKeyDefine.USER_ID, Integer.toString(userId))
                .appendQueryParameter(NetworkJsonKeyDefine.TIME, time)
                .appendQueryParameter(NetworkJsonKeyDefine.TEXT, text);
        URL url = new URL(builder.build().toString());
        return url;
    }

    @Override
    public URL getQueryUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.COMMENT).appendPath(NetworkJsonKeyDefine.QUERY)
                .appendQueryParameter(NetworkJsonKeyDefine.ID, Integer.toString(id));
        URL url = new URL(builder.build().toString());
        return url;
    }

    @Override
    public URL getUpdateUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.COMMENT).appendPath(NetworkJsonKeyDefine.UPDATE)
                .appendQueryParameter(NetworkJsonKeyDefine.ID, Integer.toString(id))
                .appendQueryParameter(NetworkJsonKeyDefine.TRAVEL_ITEM_ID, Integer.toString(travelItemId))
                .appendQueryParameter(NetworkJsonKeyDefine.USER_ID, Integer.toString(userId))
                .appendQueryParameter(NetworkJsonKeyDefine.TIME, time)
                .appendQueryParameter(NetworkJsonKeyDefine.TEXT, text);
        URL url = new URL(builder.build().toString());
        return url;
    }

    @Override
    public URL getRemoveUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.COMMENT).appendPath(NetworkJsonKeyDefine.REMOVE)
                .appendQueryParameter(NetworkJsonKeyDefine.ID, Integer.toString(id));
        URL url = new URL(builder.build().toString());
        return url;
    }

    @Override
    public URL getQueryAllUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.COMMENT).appendPath(NetworkJsonKeyDefine.QUERY_ALL)
                .appendQueryParameter(NetworkJsonKeyDefine.TRAVEL_ITEM_ID, Integer.toString(travelItemId));
        URL url = new URL(builder.build().toString());
        return url;
    }
}
