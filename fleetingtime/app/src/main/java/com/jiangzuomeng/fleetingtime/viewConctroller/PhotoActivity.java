package com.jiangzuomeng.fleetingtime.viewConctroller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jiangzuomeng.fleetingtime.R;
import com.jiangzuomeng.fleetingtime.util.BitmapUtil;

public class PhotoActivity extends AppCompatActivity {

    private ImageView photoImg;
    private EditText photoDes;

    private ImageView photoMap;
    private ImageView grayCover;

    private Button addLocation;

    private Bitmap temp2,temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        setTitle("photo edit");


        photoImg = (ImageView)findViewById(R.id.photo_detail_img);
        int width = getWindowManager().getDefaultDisplay().getWidth()-100;
        temp = BitmapFactory.decodeResource(getResources(),R.drawable.guangzhou);
        photoImg.setImageBitmap(BitmapUtil.big(temp,width,(int)((double)temp.getHeight()/(double)temp.getWidth() * (double)width)));


        photoDes = (EditText)findViewById(R.id.photo_detail_description);
        photoDes.setWidth(width);


        photoMap = (ImageView)findViewById(R.id.photo_detail_map);
        temp2 = BitmapFactory.decodeResource(getResources(),R.drawable.city);
        photoMap.setImageBitmap(BitmapUtil.big(temp2,width,200));


        grayCover = (ImageView)findViewById(R.id.photo_detail_gray_cover);
        grayCover.setMinimumWidth(width);
        grayCover.setMaxWidth(width);

        addLocation = (Button)findViewById(R.id.photo_add_location);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grayCover.setVisibility(View.GONE);
                addLocation.setVisibility(View.GONE);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_finish) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        temp2.recycle();
        temp.recycle();
    }
}
