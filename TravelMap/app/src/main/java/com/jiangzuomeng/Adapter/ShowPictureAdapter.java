package com.jiangzuomeng.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.jiangzuomeng.travelmap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wilbert on 2015/11/6.
 */
public class ShowPictureAdapter extends PagerAdapter {
    List<View> allview;
    public ShowPictureAdapter(LayoutInflater layoutInflater) {
        allview = new ArrayList<>();
        for (int i = 0; i < 4;i++) {
            View view = layoutInflater.inflate(R.layout.picture_single, null);
            allview.add(view);
        }
    }
    @Override
    public int getCount() {
        return allview.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = allview.get(position);
        ImageView imageView = (ImageView)view.findViewById(R.id.SingleImageView);
        imageView.setImageResource(R.drawable.test1_show);
        ((ViewPager)container).addView(view, position);
        return allview.get(position);
    }
}
