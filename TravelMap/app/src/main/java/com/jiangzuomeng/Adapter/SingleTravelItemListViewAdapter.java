package com.jiangzuomeng.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiangzuomeng.dataManager.DataManager;
import com.jiangzuomeng.dataManager.NetworkConnectActivity;
import com.jiangzuomeng.dataManager.NetworkHandler;
import com.jiangzuomeng.modals.TravelItem;
import com.jiangzuomeng.travelmap.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ekuri-PC on 2015/11/1.
 */
public class SingleTravelItemListViewAdapter extends BaseAdapter {
    public Bitmap getBitmapFromFilename(View view, String filename) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(
                DataManager.getLocalFile(filename)
        );
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        float bitmapHeight = options.outHeight;
        Resources resources = view.getContext().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float px = 90 * (displayMetrics.densityDpi / 160f);
        int sampleSize = (int)(bitmapHeight / px);
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        inputStream = new FileInputStream(
                DataManager.getLocalFile(filename)
        );
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    List<Map<String, Object>> data;
    public final class content {
        public ImageView image;
        public TextView title;
    }
    public Context context;
    NetworkConnectActivity targetActivity;
    public SingleTravelItemListViewAdapter(Context c, NetworkConnectActivity targetActivity) {
        data = new ArrayList<>();
        context = c;
        this.targetActivity = targetActivity;
    }

    public void setup(List<TravelItem> initialData) {
        data.clear();
        for (TravelItem item : initialData) {
            addItem(item.media, item.text);
        }
    }
    public void addItem(Object imageId, Object description) {
        Map<String, Object> map = new HashMap<>();
        map.put("image", imageId);
        map.put("title", description);
        data.add(map);
    }
    public void removeItem(int index) {
        data.remove(index);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        content z = null;
        if (convertView == null) {
            z = new content();
            convertView = LayoutInflater.from(context).inflate(R.layout.single_travel_item_layout, null);
            z.image = (ImageView)convertView.findViewById(R.id.SingleTravelItemImageView);
            z.title = (TextView)convertView.findViewById(R.id.SingleTravelItemTextView);
            convertView.setTag(z);
        } else {
            z = (content)convertView.getTag();
        }

        /*z.image.setImageResource((Integer) data.get(position).get("image"));*/
        String uriString = (String) data.get(position).get("image");
        if (uriString == null) {
            z.image.setImageResource(R.drawable.ic_mood_black_24dp);
        } else {
            try {
                z.image.setImageBitmap(getBitmapFromFilename(convertView, uriString));
            } catch (FileNotFoundException e) {
                Log.v("ekuri", "Try to download file: " + uriString);
                DataManager.getInstance(context)
                        .downLoadFile(uriString, new NetworkHandler(targetActivity));
            }
        }

        z.title.setText((String) data.get(position).get("title"));
        return convertView;
    }
}
