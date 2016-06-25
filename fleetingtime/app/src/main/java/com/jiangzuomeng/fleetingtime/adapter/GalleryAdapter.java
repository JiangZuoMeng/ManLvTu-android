package com.jiangzuomeng.fleetingtime.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;


import com.jiangzuomeng.fleetingtime.VO.Album;
import com.jiangzuomeng.fleetingtime.util.BitmapUtil;
import com.jiangzuomeng.fleetingtime.widget.GalleryFlow;

import java.util.List;

/**
 * Created by guanlu on 16/5/31.
 */
public class GalleryAdapter extends BaseAdapter {

    private Context mContext;
    private List<Album> albumList;
    private GalleryFlow galleryFlow;


    public GalleryAdapter(Context context, List<Album> albumList, GalleryFlow galleryFlow)
    {
        this.mContext = context;
        this.galleryFlow = galleryFlow;
        this.albumList = albumList;
    }
    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView temp = new ImageView(mContext);
        temp.setVisibility(View.GONE);
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new Gallery.LayoutParams(140, 300));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(BitmapUtil.createReflectedBitmap(albumList.get(position).getCover()));
        
     
        galleryFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 16/6/25 turn to album detail activity 
            }
        });

        return imageView;
    }
}