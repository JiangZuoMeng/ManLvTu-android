package com.jiangzuomeng.fleetingtime.viewConctroller;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.SimpleCardStackAdapter;
import com.jiangzuomeng.fleetingtime.R;
import com.jiangzuomeng.fleetingtime.adapter.PhotoBrowserAdapter;
import com.jiangzuomeng.fleetingtime.util.BitmapUtil;

public class PhotoBrowserActivity extends AppCompatActivity {

    private com.andtinder.view.CardContainer cardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_browser);

        cardContainer= (com.andtinder.view.CardContainer)findViewById(R.id.layoutview);
        cardContainer.setOrientation(Orientations.Orientation.Ordered);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        CardModel card = new CardModel("14", "Description goes here",
                BitmapUtil.big(BitmapFactory.decodeResource(getResources(),R.drawable.beijing),
                        (int)(width*1.5),(int)(width*1.5)));


        PhotoBrowserAdapter adapter = new PhotoBrowserAdapter(this);
        adapter.add(card);
        adapter.add(card);
        cardContainer.setAdapter(adapter);

    }
}
