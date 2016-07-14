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
public class User extends ManLvTuNetworkDataType implements ManLvTuSQLDataType {
    public static final String USER_TABLE_NAME = "user";
    public int id;
    public String username;
    public String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User() {

    }

    @Override
    public ContentValues makeSQLContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", this.username);
        contentValues.put("password", this.password);
        return contentValues;
    }

    public static String makeCreateTableSQLString() {
        return "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)";
    }

    public static User fromJson(String json, boolean withId) throws JSONException {
        User result = new User();
        JSONTokener parser = new JSONTokener(json);
        JSONObject jsonObject = (JSONObject) parser.nextValue();
        if (withId)
            result.id = jsonObject.getInt("id");
        result.username = jsonObject.getString("username");
        result.password = jsonObject.getString("password");
        return result;
    }

    public String toJson(boolean withId) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        if (withId)
            jsonObject.put("id", id);
        jsonObject.put("username", this.username);
        jsonObject.put("password", this.password);

        return jsonObject.toString();
    }


    @Override
    public URL getAddUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.USER)
                .appendPath(NetworkJsonKeyDefine.REGISTER)
                .appendQueryParameter(NetworkJsonKeyDefine.USERNAME, username)
                .appendQueryParameter(NetworkJsonKeyDefine.PASSWORD, password);
        URL url = new URL(builder.build().toString());
        return url;
    }

    @Override
    public URL getQueryUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.USER).appendPath(NetworkJsonKeyDefine.QUERY)
                .appendQueryParameter(NetworkJsonKeyDefine.ID, Integer.toString(id));
        URL url = new URL(builder.build().toString());
        return url;
    }

    @Override
    public URL getUpdateUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.USER).appendPath(NetworkJsonKeyDefine.UPDATE)
                .appendQueryParameter(NetworkJsonKeyDefine.ID, Integer.toString(id))
                .appendQueryParameter(NetworkJsonKeyDefine.USERNAME, username)
                .appendQueryParameter(NetworkJsonKeyDefine.PASSWORD, password);
        URL url = new URL(builder.build().toString());
        return url;
    }

    @Override
    public URL getRemoveUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.USER).appendPath(NetworkJsonKeyDefine.REMOVE)
                .appendQueryParameter(NetworkJsonKeyDefine.ID, Integer.toString(id));
        URL url = new URL(builder.build().toString());
        return url;
    }

    @Override
    public URL getQueryAllUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendQueryParameter(NetworkJsonKeyDefine.USERNAME, username);
        URL url = new URL(builder.build().toString());
        return url;
    }

    public URL getLoginUrl() throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.USER)
                .appendPath(NetworkJsonKeyDefine.LOGIN)
                .appendQueryParameter(NetworkJsonKeyDefine.USERNAME, username)
                .appendQueryParameter(NetworkJsonKeyDefine.PASSWORD, password);
        return new URL(builder.build().toString());
    }
}
