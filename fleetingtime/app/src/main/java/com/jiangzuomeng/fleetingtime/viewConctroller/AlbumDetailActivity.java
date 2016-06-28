package com.jiangzuomeng.fleetingtime.viewConctroller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiangzuomeng.fleetingtime.R;
import com.jiangzuomeng.fleetingtime.VO.Photo;
import com.jiangzuomeng.fleetingtime.adapter.AlbumAdapter;
import com.jiangzuomeng.fleetingtime.adapter.PhotoAdapter;
import com.jiangzuomeng.fleetingtime.util.BitmapUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlbumDetailActivity extends AppCompatActivity {

    private RecyclerView photoListView;

    private List<Photo> photoList;

    private de.hdodenhof.circleimageview.CircleImageView avatar;

    private Bitmap bg,temp;

    private android.support.design.widget.CollapsingToolbarLayout detail_bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        setTitle("Album");


        ImageView map = (ImageView)findViewById(R.id.album_detail_map);
        temp = BitmapFactory.decodeResource(getResources(),R.drawable.city);
        map.setImageBitmap(BitmapUtil.big(temp, getWindowManager().getDefaultDisplay().getWidth()-100,300));

        //photo list
        photoListView = (RecyclerView)findViewById(R.id.photo_list);
        photoListView.setLayoutManager(new LinearLayoutManager(this));
        photoListView.setItemAnimator(new DefaultItemAnimator());

        photoList = getPhotoList();

        PhotoAdapter photoAdapter = new PhotoAdapter(this,photoList);
        photoListView.setAdapter(photoAdapter);

        photoAdapter.setOnItemClickListener(new PhotoAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(AlbumDetailActivity.this, PhotoActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                //startActivityForResult(intent, 1);
                startActivity(intent);
            }
        });




        //avatar
        avatar = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.user_avatar);

        avatar.setImageBitmap(BitmapUtil.big(BitmapFactory.decodeResource(getResources(),R.drawable.avatar),65,65));


        //collapse bg
        detail_bg = (android.support.design.widget.CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);

        bg = BitmapUtil.AfterBlurring(this,BitmapFactory.decodeResource(getResources(),R.drawable.beijing),
                getWindowManager().getDefaultDisplay().getWidth(),400);

        Drawable bgr  = new BitmapDrawable(bg);

        detail_bg.setBackground(bgr);


    }

    public List<Photo> getPhotoList() {
        List<Photo> list = new ArrayList<>();
        Bitmap temp = BitmapFactory.decodeResource(getResources(),R.drawable.beijing);
        Photo photo = new Photo(BitmapUtil.big(temp,70,70),"还没有照片描述",4,new Date().toString());
        list.add(photo);
        list.add(photo);
        list.add(photo);
        list.add(photo);
        list.add(photo);
        list.add(photo);
        list.add(photo);
        temp.recycle();
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bg.recycle();
        temp.recycle();
    }
}
