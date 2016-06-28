package com.jiangzuomeng.fleetingtime.VO;

import android.graphics.Bitmap;

/**
 * Created by guanlu on 16/6/27.
 */
public class Photo {

    private Bitmap photo;
    private String description;
    private int good;
    private String time;

    public Photo(Bitmap photo, String description, int good, String time) {
        this.photo = photo;
        this.description = description;
        this.good = good;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }
}
