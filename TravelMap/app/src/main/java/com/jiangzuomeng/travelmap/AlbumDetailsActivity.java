package com.jiangzuomeng.travelmap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jiangzuomeng.dataManager.DataManager;
import com.jiangzuomeng.modals.Comment;
import com.jiangzuomeng.modals.TravelItem;
import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.util.Log;
public class AlbumDetailsActivity extends AppCompatActivity {
    ListView commentsView;
    TravelItem travelItem;
    DataManager dataManager;
    SimpleAdapter adapter;
    List<Map<String, Object>> cmmtsData;

    Handler handler;
    UserInfoHandler userInfoHandler;
    private static final String CMMNT_USER = "cmmnt_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handler = new CommentHandler();
        dataManager = DataManager.getInstance(this);

        commentsView = (ListView) findViewById(R.id.comments);
        cmmtsData = new ArrayList<>();
        adapter = new SimpleAdapter(AlbumDetailsActivity.this, cmmtsData, R.layout.album_item,
                new String[]{"album_item_userlogo", "cmmnt_user", "cmmnt_text", "cmmnt_time"},
                new int[]{R.id.album_item_userlogo, R.id.cmmnt_user, R.id.cmmnt_text, R.id.cmmnt_time});
        commentsView.setAdapter(adapter);

        Intent intent = getIntent();
        final Bundle bun = intent.getExtras();
        if (bun != null) {
            travelItem = ((TravelItem) bun.getSerializable(AlbumViewerActivity.INTERT_TRAVEL_ITEM_OBJECT));
            if (travelItem != null) {
                TextView userState = (TextView) findViewById(R.id.album_details_user_state);
                userState.setText(travelItem.text);

                TextView dateText = (TextView) findViewById(R.id.album_time);
                dateText.setText(travelItem.time);
                dataManager.queryCommentIdListByTravelItemId(travelItem.id, handler);
            }
        }
        final EditText myComment = (EditText) findViewById(R.id.album_my_comments);

        commentsView = (ListView) findViewById(R.id.comments);
        Button sendCmmtBtn = (Button) findViewById(R.id.send_cmmnt_btn);
        sendCmmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment newComment = new Comment();
                newComment.travelItemId = travelItem.id;
                newComment.text = myComment.getText().toString();
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                newComment.time = android.text.format.DateFormat.format("hh:mm MM/dd", date).toString();
                newComment.userId = MainActivity.userId;

                Map<String, Object> itemData = new HashMap<>();
                itemData.put("album_item_userlogo", R.drawable.default_usr_logo);
                itemData.put(CMMNT_USER, newComment.userId);
                itemData.put("cmmnt_text", newComment.text);
                itemData.put("cmmnt_time", newComment.time);
                cmmtsData.add(itemData);

                adapter = new SimpleAdapter(AlbumDetailsActivity.this, cmmtsData, R.layout.album_item,
                        new String[]{"album_item_userlogo", "cmmnt_user", "cmmnt_text", "cmmnt_time"},
                        new int[]{R.id.album_item_userlogo, R.id.cmmnt_user, R.id.cmmnt_text, R.id.cmmnt_time});
                commentsView.setAdapter(adapter);
                UserInfoHandler newMsgHandler = new UserInfoHandler(cmmtsData.size());
                newMsgHandler.setCurIdx(cmmtsData.size() - 1);
                dataManager.queryUserByUserId(newComment.userId, newMsgHandler);

                // TODO 暂不处理失败
                dataManager.addNewComment(newComment, new Handler());
                myComment.setText("");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }


    private class CommentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NetworkJsonKeyDefine.NETWORK_OPERATION:
                    try {
                        Bundle bundle = msg.getData();
                        String responseStr = bundle.getString(NetworkJsonKeyDefine.NETWORK_RESULT_KEY);
                        JSONTokener tokener = new JSONTokener(responseStr);
                        JSONArray cmmtsArr = ((JSONObject) tokener.nextValue()).getJSONArray("data");
                        List<Comment> cmmts = new ArrayList<>();
                        for (int i = 0; i < cmmtsArr.length(); ++i) {
                            cmmts.add(Comment.fromJson(cmmtsArr.getString(i), true));
                        }
                        onCommentsUpdate(cmmts);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        private void onCommentsUpdate(List<Comment> cmmts) {
            List<Map<String, Object>> data = new ArrayList<>();
            for (Comment cmt : cmmts) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("album_item_userlogo", R.drawable.default_usr_logo);
                itemData.put(CMMNT_USER, cmt.userId);
                itemData.put("cmmnt_text", cmt.text);
                itemData.put("cmmnt_time", cmt.time);
                data.add(itemData);
            }
            cmmtsData = data;
            adapter = new SimpleAdapter(AlbumDetailsActivity.this, cmmtsData, R.layout.album_item,
                    new String[]{"album_item_userlogo", CMMNT_USER, "cmmnt_text", "cmmnt_time"},
                    new int[]{R.id.album_item_userlogo, R.id.cmmnt_user, R.id.cmmnt_text, R.id.cmmnt_time});
            commentsView.setAdapter(adapter);
            userInfoHandler = new UserInfoHandler(cmmts.size());
            if (cmmts.size() > 0) {
                dataManager.queryUserByUserId(cmmts.get(0).userId, userInfoHandler);
            }
        }
    }


    private class UserInfoHandler extends Handler {
        int userCount;
        int curIdx;
        public UserInfoHandler(int userCount) {
            super();
            this.userCount = userCount;
            curIdx = 0;
        }

        public void setCurIdx(int idx) {
            curIdx = idx;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NetworkJsonKeyDefine.NETWORK_OPERATION:
                    Bundle bundle = msg.getData();
                    String jsonStr = bundle.getString(NetworkJsonKeyDefine.NETWORK_RESULT_KEY);
                    JSONTokener tokener = new JSONTokener(jsonStr);
                    try {
                        JSONObject userObject = ((JSONObject) tokener.nextValue()).getJSONObject(NetworkJsonKeyDefine.DATA_KEY);
                        String userName = userObject.getString(NetworkJsonKeyDefine.USERNAME);
                        cmmtsData.get(curIdx).put(CMMNT_USER, userName);
                        adapter.notifyDataSetChanged();
                        ++curIdx;
                        if (curIdx < userCount) {
                            int userid = (Integer) (cmmtsData.get(curIdx).get(CMMNT_USER));
                            dataManager.queryUserByUserId(userid, this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

}
