package com.jiangzuomeng.travelmap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    ListView noti_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_notification);

        noti_list = (ListView) findViewById(R.id.notificaton_list);

        SimpleAdapter adapter = new SimpleAdapter(this, getTmpData(), R.layout.notification_item,
                new String[]{"sender_logo", "sender_text", "send_message", "send_time"},
                new int[]{R.id.album_item_userlogo, R.id.cmmnt_user, R.id.cmmnt_text, R.id.cmmnt_time});
        noti_list.setAdapter(adapter);
    }

    private List<Map<String, Object>> getTmpData() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("sender_logo", R.drawable.default_usr_logo);
        itemData.put("sender_text", "Chrom");
        itemData.put("send_message", "内存不够吃");
        itemData.put("send_time", "2014.12.Nov");
        data.add(itemData);

        return data;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }
}
