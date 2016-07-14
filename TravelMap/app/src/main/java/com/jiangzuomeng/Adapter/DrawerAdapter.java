package com.jiangzuomeng.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiangzuomeng.dataManager.DataManager;
import com.jiangzuomeng.dataManager.NetworkConnectActivity;
import com.jiangzuomeng.dataManager.NetworkHandler;
import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;
import com.jiangzuomeng.travelmap.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wilbert on 2015/10/31.
 */
public class DrawerAdapter  extends BaseAdapter{
    public static final  String TITLE = "title";
    public static final  String IMAGE = "image";
    public static final  String TRAVEL_ID = "travelId";
    List<Map<String, Object>> data;
    List<String> uriList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    List<Bitmap> bitmapList = new ArrayList<>();
    List<Integer> travelIdList = new ArrayList<>();
    public final class content {
        public ImageView image;
        public TextView title;
    }
    public Context context;
    NetworkConnectActivity targetActivity;

    public DrawerAdapter(List<Integer> travelIdList, List<String> uriList, List<String> nameList, Context c,
                         NetworkConnectActivity networkConnectActivity) {
        data = new ArrayList<>();
        context = c;

        this.uriList = uriList;
        this.nameList = nameList;
        this.travelIdList = travelIdList;
        this.targetActivity = networkConnectActivity;
        initData();
    }

    private void initData() {
        //// TODO: 2015/11/21

        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < nameList.size(); i++) {
            map = new HashMap<>();
            map.put(TITLE, nameList.get(i));
            map.put(IMAGE, uriList.get(i));
            map.put(TRAVEL_ID, travelIdList.get(i));
            data.add(map);
        }
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_single_drawer, null);
            z.image = (ImageView)convertView.findViewById(R.id.drawer_image);
            z.title = (TextView)convertView.findViewById(R.id.drawer_text);
            convertView.setTag(z);
        } else {
            z = (content)convertView.getTag();
        }
        String uriString = (String)data.get(position).get("image");
        if (uriString == null) {
            z.image.setImageResource(R.drawable.test1_show);
        } else {
            try {
                z.image.setImageBitmap(DataManager.getInstance(context).getBitmapFromUri(
                        DataManager.getUriFromImageName(uriString), 150
                ));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                DataManager.getInstance(context)
                        .downLoadFile(uriString, new NetworkHandler(targetActivity));
            }
        }
        z.image.setScaleType(ImageView.ScaleType.CENTER);
        z.title.setText((String) data.get(position).get(TITLE));
        return convertView;
    }
    public List<Integer> getTravelIdList() {
        return travelIdList;
    }
    public void removeAt(int i) {
        travelIdList.remove(i);
        uriList.remove(i);
        nameList.remove(i);
    }
}
