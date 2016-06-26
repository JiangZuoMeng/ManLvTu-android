package com.jiangzuomeng.fleetingtime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiangzuomeng.fleetingtime.R;
import com.jiangzuomeng.fleetingtime.VO.HotAlbum;
import com.jiangzuomeng.fleetingtime.util.BitmapUtil;

import java.util.List;

/**
 * Created by guanlu on 16/6/26.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {

    private List<HotAlbum> list;
    private Context context;

    public AlbumAdapter(Context context,List<HotAlbum> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public AlbumHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.circle_album_card,viewGroup,false);
        return new AlbumHolder(v);
    }


    @Override
    public void onBindViewHolder(AlbumHolder viewHolder, int i ) {
        HotAlbum album = list.get(i);

        viewHolder.cover.setImageBitmap(BitmapUtil.getRoundedCornerBitmap(album.getAlbumCover(),10.0f));

        viewHolder.name.setText(album.getAlbumName());

        viewHolder.description.setText(album.getAlbumDescription());

        viewHolder.location.setText(album.getAlbumLocation());

        viewHolder.goodNum.setText(album.getAlbumNumOfGood());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class AlbumHolder extends RecyclerView.ViewHolder {

        public ImageView cover;
        public TextView name;
        public TextView goodNum;
        public TextView location;
        public TextView description;

        public  AlbumHolder(View v) {
            super(v);

            name = (TextView)v.findViewById(R.id.album_name);
            goodNum = (TextView)v.findViewById(R.id.album_good_num);
            location = (TextView)v.findViewById(R.id.album_location);
            description = (TextView)v.findViewById(R.id.album_description);
            cover = (ImageView)v.findViewById(R.id.album_cover);
        }
    }
}
