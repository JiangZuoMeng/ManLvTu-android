package com.jiangzuomeng.fleetingtime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiangzuomeng.fleetingtime.R;
import com.jiangzuomeng.fleetingtime.VO.Photo;
import com.jiangzuomeng.fleetingtime.util.BitmapUtil;

import java.util.List;

/**
 * Created by guanlu on 16/6/27.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder>{


    private List<Photo> list;
    private Context context;
    private MyItemClickListener mItemClickListener;

    public PhotoAdapter(Context context,List<Photo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_card,viewGroup,false);
        return new PhotoHolder(v,mItemClickListener);
    }


    @Override
    public void onBindViewHolder(PhotoHolder viewHolder, int i ) {
        Photo photo = list.get(i);

        viewHolder.photo.setImageBitmap(photo.getPhoto());

        viewHolder.description.setText(photo.getDescription());

        viewHolder.time.setText(photo.getTime());

    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView photo;
        public TextView description;
        public TextView time;

        private MyItemClickListener mListener;

        public  PhotoHolder(View v,MyItemClickListener listener) {
            super(v);

            photo = (ImageView) v.findViewById(R.id.photo_img);
            description = (TextView)v.findViewById(R.id.photo_description);
            time = (TextView)v.findViewById(R.id.photo_time);

            this.mListener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getPosition());
            }
        }


    }

    public interface MyItemClickListener {
        public void onItemClick(View view,int position);
    }


}

