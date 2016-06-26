package com.jiangzuomeng.fleetingtime.VO;

import android.graphics.Bitmap;

/**
 * Created by guanlu on 16/6/26.
 */
public class HotAlbum {

    private Bitmap albumCover;
    private String albumName;
    private String albumLocation;
    private String albumDescription;
    private String albumNumOfGood;

    public HotAlbum(Bitmap albumCover, String albumName, String albumLocation, String albumDescription, String albumNumOfGood) {
        this.albumCover = albumCover;
        this.albumName = albumName;
        this.albumLocation = albumLocation;
        this.albumDescription = albumDescription;
        this.albumNumOfGood = albumNumOfGood;
    }

    public Bitmap getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(Bitmap albumCover) {
        this.albumCover = albumCover;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumLocation() {
        return albumLocation;
    }

    public void setAlbumLocation(String albumLocation) {
        this.albumLocation = albumLocation;
    }

    public String getAlbumDescription() {
        return albumDescription;
    }

    public void setAlbumDescription(String albumDescription) {
        this.albumDescription = albumDescription;
    }

    public String getAlbumNumOfGood() {
        return albumNumOfGood;
    }

    public void setAlbumNumOfGood(String albumNumOfGood) {
        this.albumNumOfGood = albumNumOfGood;
    }
}
