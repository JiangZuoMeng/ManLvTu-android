package com.jiangzuomeng.fleetingtime.viewConctroller;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiangzuomeng.fleetingtime.R;
import com.jiangzuomeng.fleetingtime.VO.Album;
import com.jiangzuomeng.fleetingtime.adapter.GalleryAdapter;
import com.jiangzuomeng.fleetingtime.widget.GalleryFlow;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {

    private View view;

    private static final String TAG = "ALBUM";


    //album gallery
    private GalleryFlow galleryFlow;

    private List<Album> albumList = new ArrayList<>();

    private List<Bitmap> tempBitmaps = new ArrayList<>();

    private GalleryAdapter galleryAdapter;


    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(getResources().getString(R.string.album));
        System.out.println(getActivity().getTitle());

        view =  inflater.inflate(R.layout.fragment_album, container, false);


        //album gallery
        galleryFlow = (GalleryFlow)view.findViewById(R.id.gallery);
        albumList= getAlbumList();
        galleryAdapter = new GalleryAdapter(getContext(),albumList,galleryFlow);

        galleryFlow.setAdapter(galleryAdapter);

        return view;
    }




    private List<Album> getAlbumList() {
        List<Album> albumList = new ArrayList<>();

        tempBitmaps.add(BitmapFactory.decodeResource(getResources(),R.drawable.beijing));
        tempBitmaps.add(BitmapFactory.decodeResource(getResources(),R.drawable.guangzhou));
        Album album1 = new Album("北京", tempBitmaps.get(0));
        albumList.add(album1);
        Album album2 = new Album("广州", tempBitmaps.get(1));
        albumList.add(album2);
        albumList.add(album1);
        albumList.add(album2);

        return albumList;
    };

    @Override
    public void onHiddenChanged(boolean hidd) {
        if(!hidd) {
            System.out.println(TAG);
            getActivity().setTitle(getResources().getString(R.string.album));
            getAlbumList();

        }
        if(hidd) {
            for (Bitmap bit : tempBitmaps) {
                bit.recycle();

            }
        }
    }
}
