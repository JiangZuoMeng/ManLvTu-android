package com.jiangzuomeng.fleetingtime.VO;

import android.graphics.Bitmap;

/**
 * Created by guanlu on 16/6/25.
 */
public class Album {
    private String name;
    private Bitmap cover;

    public Album(String name, Bitmap cover) {
        this.name = name;
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }
}
