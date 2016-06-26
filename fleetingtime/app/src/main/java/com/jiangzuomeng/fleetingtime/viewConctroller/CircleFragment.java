package com.jiangzuomeng.fleetingtime.viewConctroller;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiangzuomeng.fleetingtime.R;
import com.jiangzuomeng.fleetingtime.VO.HotAlbum;
import com.jiangzuomeng.fleetingtime.adapter.AlbumAdapter;
import com.jiangzuomeng.fleetingtime.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CircleFragment extends Fragment {

    private View view;

    private static final String TAG = "CIRCLE";

    private RecyclerView albumListView;

    private List<HotAlbum> albumList;

    private List<Bitmap> bitmaps;
    public CircleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getResources().getString(R.string.circle));
        System.out.println(getActivity().getTitle());
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_circle, container, false);

        //album list
        albumListView=(RecyclerView)view.findViewById(R.id.album_list);
        albumListView.setLayoutManager(new LinearLayoutManager(getContext()));
        albumListView.setItemAnimator(new DefaultItemAnimator());

        albumList = getAlbumList();
        AlbumAdapter albumAdapter = new AlbumAdapter(getContext(),albumList);
        albumListView.setAdapter(albumAdapter);
        return view;
    }

    private List<HotAlbum> getAlbumList() {
        List<HotAlbum> list = new ArrayList<>();
        bitmaps = new ArrayList<>();
        Bitmap temp =BitmapFactory.decodeResource(getResources(),R.drawable.guangzhou);
        bitmaps.add(BitmapUtil.big(temp,70,70));
        temp.recycle();
        HotAlbum album = new HotAlbum(bitmaps.get(0),"广州人文","广州番禺区","大学城十所高校的人文景色","4");
        list.add(album);
        list.add(album);
        list.add(album);



        return list;
    }



    @Override
    public void onHiddenChanged(boolean hidd) {
        if(!hidd) {
            System.out.println(TAG);
            getActivity().setTitle(getResources().getString(R.string.circle));

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Bitmap bit : bitmaps) {
            bit.recycle();

        }
    }
}
