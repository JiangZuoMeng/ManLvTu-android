package com.jiangzuomeng.fleetingtime.network;


import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wilbert on 2015/12/13.
 */
public abstract class ManLvTuNetworkDataType {
    /*public URL getUrl(String key) throws MalformedURLException {
        URL url = null;
        switch (key) {
            case NetworkJsonKeyDefine.ADD:
                url = getAddUrl();
                break;
            case NetworkJsonKeyDefine.UPDATE:
                url =  getUpdateUrl();
                break;
            case NetworkJsonKeyDefine.REMOVE:
                url = getRemoveUrl();
                break;
            case NetworkJsonKeyDefine.QUERY:
                url = getQueryUrl();
                break;
            case NetworkJsonKeyDefine.QUERY_ALL:
                url = getQueryAllUrl();
                break;
        }
        return url;
    }*/

    public abstract URL getAddUrl() throws MalformedURLException;
    public abstract URL getQueryUrl() throws MalformedURLException;
    public abstract URL getUpdateUrl() throws MalformedURLException;
    public abstract URL getRemoveUrl() throws MalformedURLException;
    public abstract URL getQueryAllUrl() throws MalformedURLException;
}
