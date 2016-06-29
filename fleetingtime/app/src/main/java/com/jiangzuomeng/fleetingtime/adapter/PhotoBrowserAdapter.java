package com.jiangzuomeng.fleetingtime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardStackAdapter;
import com.jiangzuomeng.fleetingtime.R;

/**
 * Created by guanlu on 16/6/29.
 */
public class PhotoBrowserAdapter extends CardStackAdapter {

    private TextView tv;
    private int num = 0;

    public PhotoBrowserAdapter(Context mContext) {
            super(mContext);
        }

    public View getCardView(int position, final CardModel model, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(this.getContext());

                convertView = inflater.inflate(R.layout.photo_browser_card, parent, false);

                assert convertView != null;
            }

            ((ImageView)convertView.findViewById(R.id.photo_browser_img)).setImageDrawable(model.getCardImageDrawable());
            ((TextView)convertView.findViewById(R.id.photo_browser_description)).setText(model.getDescription());
            tv = (TextView)convertView.findViewById(R.id.photo_browser_favorite_num);
            tv.setText(model.getTitle());
            try {
                num = Integer.getInteger(model.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
            final ImageButton btn = (ImageButton)convertView.findViewById(R.id.photo_browser_favorite);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn.setBackgroundResource(R.drawable.ic_favorite_click_18dp);
                    AnimationSet animationSet = new AnimationSet(true);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,2.0f,1.0f,2.0f,Animation.RELATIVE_TO_SELF,0.5f
                    ,Animation.RELATIVE_TO_SELF,0.5f);
                    animationSet.addAnimation(scaleAnimation);
                    scaleAnimation.setDuration(500);
                    scaleAnimation.setInterpolator(new AccelerateInterpolator());
                    btn.startAnimation(animationSet);
                    tv.setText(Integer.toString(num++));



                }
            });
            return convertView;
        }
}

