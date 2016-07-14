package com.jiangzuomeng.modals;

import android.content.ContentValues;
import android.net.Uri;

import com.jiangzuomeng.database.ManLvTuSQLDataType;
import com.jiangzuomeng.networkManager.ManLvTuNetworkDataType;
import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ekuri-PC on 2015/11/21.
 */
public class TravelItem extends ManLvTuNetworkDataType implements ManLvTuSQLDataType, Serializable {
    public static final String TRAVEL_ITEM_TABLE_NAME = "travelItem";
    public int id;
    public int travelId;
    public String label;
    public String time;
    public double locationLng;
    public double locationLat;
    public int like;
    public String text;
    public String media;

    public TravelItem() {

    }

    @Override
    public ContentValues makeSQLContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("travelId", this.travelId);
        contentValues.put("label", this.label);
        contentValues.put("time", this.time);
        contentValues.put("locationLng", this.locationLng);
        contentValues.put("locationLat", this.locationLat);
        contentValues.put("like", this.like);
        contentValues.put("text", this.text);
        contentValues.put("media", this.media);
        return contentValues;
    }

    public static String makeCreateTableSQLString() {
        return "CREATE TABLE IF NOT EXISTS " + TRAVEL_ITEM_TABLE_NAME +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, travelId INTEGER," +
                "label TEXT, time TEXT, locationLat REAL, locationLng REAL, like INTEGER," +
                "text TEXT, media TEXT)";
    }

    public static TravelItem fromJson(String json, boolean withId) throws JSONException {
        TravelItem result = new TravelItem();
        JSONTokener parser = new JSONTokener(json);
        JSONObject jsonObject = (JSONObject) parser.nextValue();
        if (withId)
            result.id = jsonObject.getInt("id");
        result.travelId = jsonObject.getInt("travelId");
        result.label = jsonObject.getString("label");
        result.time = jsonObject.getString("time");
        result.locationLat = jsonObject.getDouble("locationLat");
        result.locationLng = jsonObject.getDouble("locationLng");
        result.like = jsonObject.getInt("like");
        result.text = jsonObject.getString("text");
        result.media = jsonObject.getString("media");
        return result;
    }

    public String toJson(boolean withId) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        if (withId)
            jsonObject.put("id", id);
        jsonObject.put("travelId", this.travelId);
        jsonObject.put("label", this.label);
        jsonObject.put("time", this.time);
        jsonObject.put("locationLat", this.locationLat);
        jsonObject.put("locationLng", this.locationLng);
        jsonObject.put("like", this.like);
        jsonObject.put("text", this.text);
        jsonObject.put("media", this.media);

        return jsonObject.toString();
    }

    @Override
    public URL getAddUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.TRAVEL_ITEM)
                .appendPath(NetworkJsonKeyDefine.ADD)
                .appendQueryParameter(NetworkJsonKeyDefine.TRAVEL_ID, Integer.toString(travelId))
                .appendQueryParameter(NetworkJsonKeyDefine.LABEL, label)
                .appendQueryParameter(NetworkJsonKeyDefine.TIME, time)
                .appendQueryParameter(NetworkJsonKeyDefine.LOCATION_LAT, Double.toString(locationLat))
                .appendQueryParameter(NetworkJsonKeyDefine.LOCATION_LNG, Double.toString(locationLng))
                .appendQueryParameter(NetworkJsonKeyDefine.LIKE, Integer.toString(like))
                .appendQueryParameter(NetworkJsonKeyDefine.TEXT, text)
                .appendQueryParameter(NetworkJsonKeyDefine.MEDIA, media);
        URL url = new URL(builder.build().toString());
        return url;
    }

    @Override
    public URL getQueryUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.TRAVEL_ITEM).appendPath(NetworkJsonKeyDefine.QUERY)
                .appendQueryParameter(NetworkJsonKeyDefine.ID, Integer.toString(id));
        URL url = new URL(builder.build().toString());
        return url;
    }

    @Override
    public URL getUpdateUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.TRAVEL_ITEM).appendPath(NetworkJsonKeyDefine.UPDATE)
                .appendQueryParameter(NetworkJsonKeyDefine.ID, Integer.toString(id))
                .appendQueryParameter(NetworkJsonKeyDefine.TRAVEL_ID, Integer.toString(travelId))
                .appendQueryParameter(NetworkJsonKeyDefine.LABEL, label)
                .appendQueryParameter(NetworkJsonKeyDefine.TIME, time)
                .appendQueryParameter(NetworkJsonKeyDefine.LOCATION_LAT, Double.toString(locationLat))
                .appendQueryParameter(NetworkJsonKeyDefine.LOCATION_LNG, Double.toString(locationLng))
                .appendQueryParameter(NetworkJsonKeyDefine.LIKE, Integer.toString(like))
                .appendQueryParameter(NetworkJsonKeyDefine.TEXT, text)
                .appendQueryParameter(NetworkJsonKeyDefine.MEDIA, media);
        URL url = new URL(builder.build().toString());
        return url;
    }

    @Override
    public URL getRemoveUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.TRAVEL_ITEM).appendPath(NetworkJsonKeyDefine.REMOVE)
                .appendQueryParameter(NetworkJsonKeyDefine.ID, Integer.toString(id));
        URL url = new URL(builder.build().toString());
        return url;
    }

    @Override
    public URL getQueryAllUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.TRAVEL_ITEM).appendPath(NetworkJsonKeyDefine.QUERY_ALL)
                .appendQueryParameter(NetworkJsonKeyDefine.TRAVEL_ID, Integer.toString(travelId));
        URL url = new URL(builder.build().toString());
        return url;
    }

    public static URL getQueryNearbyUrl(double locationLatLowerBound, double locationLatUpperBound,
                                        double locationLngLowerBound, double locationLngUpperBound) throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.TRAVEL_ITEM).appendPath(NetworkJsonKeyDefine.QUERY_NEARBY)
                .appendQueryParameter(NetworkJsonKeyDefine.LOCATION_LAT_LOWER_BOUND, Double.toString(locationLatLowerBound))
                .appendQueryParameter(NetworkJsonKeyDefine.LOCATION_LAT_UPPER_BOUND, Double.toString(locationLatUpperBound))
                .appendQueryParameter(NetworkJsonKeyDefine.LOCATION_LNG_LOWER_BOUND, Double.toString(locationLngLowerBound))
                .appendQueryParameter(NetworkJsonKeyDefine.LOCATION_LNG_UPPER_BOUND, Double.toString(locationLngUpperBound));
        return new URL(builder.build().toString());
    }
}
