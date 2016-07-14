package com.jiangzuomeng.travelmap;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.jiangzuomeng.Adapter.DrawerAdapter;
import com.jiangzuomeng.Adapter.SetTagAdapter;
import com.jiangzuomeng.MyLayout.CustomViewPager;
import com.jiangzuomeng.dataManager.DataManager;
import com.jiangzuomeng.dataManager.NetworkConnectActivity;
import com.jiangzuomeng.dataManager.NetworkHandler;
import com.jiangzuomeng.modals.Travel;
import com.jiangzuomeng.modals.TravelItem;
import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

enum State{
        OnTrip,NotOnTrip
        }
public class MainActivity extends AppCompatActivity implements AMapFragment.MainActivityListener,
                                                    NetworkConnectActivity{
    public  static final String LOCATION_LNG_KEY = "locationlng";
    public  static final String LOCATION_LAT_KEY = "locationlat";
    public static int currentTravelId;
    public static  int userId;
    String userName;
    CollectionPagerAdapter pagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    State state;
    double locationLng = 0;
    double locationLat = 0;
    FloatingActionButton fab;
    FloatingActionButton tag;
    String []strs = new String[] {
            "遇见","美食","酒店"
    };
    ListView.OnItemClickListener onItemClickListener = null;
    ListView.OnItemLongClickListener onItemLongClickListener;
    TabLayout.OnTabSelectedListener onTabSelectedListener;
    ViewPager.SimpleOnPageChangeListener simpleOnPageChangeListener;
    Button.OnClickListener btnOnclickListener;
    Button.OnLongClickListener btnOnLongClickListener;

    MainActivity mainActivity;
    Button addOtherTagBtn;
    EditText addTagEditText;
    SetTagAdapter setTagAdapter;
    ListView listView_drawer;
    ListView setTagListView = null;

    DrawerAdapter drawerAdapter;

    DataManager dataManager;
    List<Travel> travelList;
    List<Integer> travelIdList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    List<String> uriList = new ArrayList<>();
    Handler handler = new Handler();
    NetworkHandler networkHandler = new NetworkHandler(this);

    DrawerLayout drawer;
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataManager = DataManager.getInstance(getApplicationContext());
        state = State.NotOnTrip;

        userId = getIntent().getIntExtra(NetworkJsonKeyDefine.ID, -1);
        Log.v("wilbert", "userId:" + Integer.toString(userId));
        mainActivity = this;
        initMyListener();
        pagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager(),2);
        viewPager = (CustomViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);
        viewPager.setOnPageChangeListener(simpleOnPageChangeListener);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setLongClickable(true);
        fab.setOnClickListener(btnOnclickListener);
        fab.setOnLongClickListener(btnOnLongClickListener);
        fab.setBackgroundResource(R.drawable.ic_add_black_24dp);

        tag = (FloatingActionButton) findViewById(R.id.set_tag);
        tag.setOnClickListener(btnOnclickListener);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        listView_drawer = (ListView)findViewById(R.id.drawer_listview);
        listView_drawer.setAdapter(drawerAdapter);
        listView_drawer.setOnItemClickListener(onItemClickListener);
        listView_drawer.setOnItemLongClickListener(onItemLongClickListener);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setText("No Travel On");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (popupWindowAtTab != null) {
                    popupWindowAtTab.dismiss();
                }
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                showPopupWindowTips();
                if (popupWindowAtTab != null) {
                    popupWindowAtTab.showAsDropDown(tabLayout);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            showPopupWindowTips();
        }
    }

    PopupWindow popupWindow;
    PopupWindow popupWindowAtTab;
    private void showPopupWindowTips() {
        TextView textView = new TextView(this);
        textView.getPaint().setFakeBoldText(true);
        textView.setBackgroundColor(Color.rgb(113, 239, 255));
//        textView.setTextColor(Color.rgb(113, 239, 255));
        if (state == State.NotOnTrip) {
            textView.setText("点击开启新的旅程");
        } else {
            textView.setText("点击创建心情,长按结束旅程");
        }
        if (popupWindow != null)
        popupWindow.dismiss();

        popupWindow = new PopupWindow(textView, ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, false);

        textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = textView.getMeasuredWidth();
        int popupHeight =  textView.getMeasuredHeight();
        int[] location = new int[2];
        fab.getLocationOnScreen(location);
        popupWindow.showAtLocation(fab, Gravity.NO_GRAVITY,
                (location[0] + fab.getWidth()/2)-popupWidth/2,
                location[1]-popupHeight);

//        popupWindow.showAsDropDown(fab);

        if (popupWindowAtTab != null) {
//            popupWindowAtTab.dismiss();
            return;
        }
        TextView textViewAtTab = new TextView(this);
        textViewAtTab.setBackgroundColor(Color.rgb(113, 239, 255));
//        textViewAtTab.setTextColor(Color.rgb(113, 239, 255));
        textViewAtTab.getPaint().setFakeBoldText(true);
        textViewAtTab.setText("左边是正在进行的旅图,右边是所有的旅图\r\n侧滑有惊喜");
        popupWindowAtTab = new PopupWindow(textViewAtTab, ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, false);
        popupWindowAtTab.showAsDropDown(tabLayout);
    }

    private void initdrawerAdapter() {
        uriList.clear();
        nameList.clear();
        travelIdList.clear();

        for (int i = 0; i < 50; i++) {
            uriList.add(null);
        }

        dataManager.queryTravelIdListByUserId(MainActivity.userId, networkHandler);
/*
        travelList = dataManager.queryTravelListByUserId(userId);
        List<Uri> uriList = new ArrayList<>();
        List<Integer> travelIdList = new ArrayList<>();
        for (Travel travel : travelList) {
            Uri uri = null;
            List<TravelItem> travelItemList = dataManager.queryTravelItemListByTravelId(travel.id);
            if (!travelItemList.isEmpty()) {
                TravelItem travelItem = travelItemList.get(0);
                if (travelItem.media != null) {
                    uri = Uri.parse(travelItem.media);
                }
            }
            uriList.add(uri);
            nameList.add(travel.name);
            travelIdList.add(travel.id);
        }
        drawerAdapter = new DrawerAdapter(travelIdList, uriList, nameList, this);
*/
        List<String> strings = new ArrayList<>();
        strings.add("此部分未完成,选择后无实际效果");
        for (int i = 0; i < 5; i++)
            strings.add(Integer.toString(i));
        setTagAdapter = new SetTagAdapter(strings, this);
    }

    private void initMyListener() {
        onItemClickListener = new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,SingleTravelActivity.class);
                intent.putExtra(SingleTravelActivity.INTENT_TRAVEL_KEY, travelIdList.get(position));
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(intent);
            }
        };

        onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };

        simpleOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                tab.select();
            }

        };

        btnOnclickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fab:
                        fab_short_click(v);
                        break;
                    case R.id.set_tag:
                        set_tag_click(v);
                        break;
                    case R.id.addOtherTagBtn:
                        addOtherTag();
                }
            }
        };

        btnOnLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fab_long_click(v);
                fab.setBackgroundResource(R.drawable.ic_add_black_24dp);
                return true;
            }
        };
        onItemLongClickListener = new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                switch (parent.getId()) {
                    case R.id.tag_listView:
                        setTagAdapter.removeAt(position);
                        setTagAdapter.notifyDataSetChanged();
                        break;
                    case R.id.drawer_listview:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(R.string.delete_confirm);
                        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: 2015/12/14 network
                                /*dataManager.removeTravelByTravelId(drawerAdapter.getTravelIdList().
                                        get(position));*/
                                //Log.v("ekuri", "on item click listener");
                                //initdrawerAdapter();
                                //listView_drawer.setAdapter(drawerAdapter);
                                dataManager.removeTravelByTravelId(drawerAdapter.getTravelIdList().get(position)
                                , networkHandler);
                            }
                        });
                        builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                        break;
                    default:
                    }
                return true;
            }
        };
    }

    private void addOtherTag() {
        if(!addTagEditText.getText().toString().equals("")) {
            setTagAdapter.getStrings().add(addTagEditText.getText().toString());
            setTagAdapter.getIsSelectList().add(setTagAdapter.getStrings().size() - 1, true);
            setTagAdapter.notifyDataSetChanged();
            addTagEditText.setText("");
        }
    }

    private void set_tag_click(View v) {
        View popView = getLayoutInflater().inflate(R.layout.popup_set_tag, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popView);
        builder.setTitle("请选择筛选标签");
        setTagListView = (ListView)popView.findViewById(R.id.tag_listView);
        addOtherTagBtn = (Button)popView.findViewById(R.id.addOtherTagBtn);
        addOtherTagBtn.setOnClickListener(btnOnclickListener);
        addTagEditText = (EditText)popView.findViewById(R.id.AddTagEditText);
        setTagListView.setAdapter(setTagAdapter);
        setTagListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SetTagAdapter.ViewHolder viewHolder = (SetTagAdapter.ViewHolder) view.getTag();
                viewHolder.checkBox.toggle();
                setTagAdapter.getIsSelectList().set(position, viewHolder.checkBox.isChecked());
//                tag_on_click_listener(position);
            }
        });
        setTagListView.setOnItemLongClickListener(onItemLongClickListener);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

/*    private void tag_on_click_listener(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setText(strs[position]);
        setTagPopUpWindow.dismiss();
    }*/

    private void fab_long_click(View v) {
        if (state == State.OnTrip) {
            //stop the trip
//            Snackbar.make(v,"stop the trip", Snackbar.LENGTH_SHORT).show();
            Toast toast = Toast.makeText(getApplicationContext(), "the trip has stopped", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            toast.show();
            state = State.NotOnTrip;
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.setText("No Travel On");
            fab.setBackgroundResource(R.drawable.ic_add_black_24dp);
            showPopupWindowTips();
        } else {

        }
    }

    private void fab_short_click(View view) {
        if (state == State.NotOnTrip) {
        View popView= getLayoutInflater().inflate(R.layout.popup_create_travel,null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText nameEdittext = (EditText)popView.findViewById(R.id.travelNameEditText);
            builder.setView(popView);
            builder.setIcon(R.mipmap.apple_touch_icon);
            builder.setTitle(R.string.dialogTitle);
            builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (nameEdittext.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(MainActivity.this, "请输入旅途名称", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                        toast.show();
                        return;
                    }
                    state = State.OnTrip;
                    Travel travel = new Travel();
                    travel.userId = userId;
                    travel.name = nameEdittext.getText().toString();
                    dataManager.addNewTravel(travel, new NetworkHandler(mainActivity));
                }
            });
            builder.show();
        }
        else {
            Intent intent = new Intent(this, CreateNewItemActivity.class);
            Bundle bundle = new Bundle();
            bundle.putDouble(LOCATION_LAT_KEY, locationLat);
            bundle.putDouble(LOCATION_LNG_KEY, locationLng);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.v("ekuri", "on resume");
        initdrawerAdapter();
        listView_drawer.setAdapter(drawerAdapter);

        Message message = new Message();
        message.what = AMap_MySelf_Fragment.UPDATE;
        Bundle bundle = new Bundle();
        bundle.putDouble(LOCATION_LAT_KEY, locationLat);
        bundle.putDouble(LOCATION_LNG_KEY, locationLng);
        message.setData(bundle);
        handler.sendMessage(message);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
        }
        return true;
    }

    @Override
    public void notifyLocation(double locationLng, double locationLat) {
        this.locationLat = locationLat;
        this.locationLng = locationLng;
    }

    private int queryTravelItemCount = 0;
    @Override
    public void handleNetworkEvent(String result, String request, String target, JSONObject originJSONObject) throws JSONException {
        switch (request) {
            case NetworkJsonKeyDefine.FILE_DOWNLOAD:
                if (result.equals(NetworkJsonKeyDefine.RESULT_SUCCESS)) {
                    listView_drawer.setAdapter(drawerAdapter);
                }
                break;
            case NetworkJsonKeyDefine.ADD:
                switch (target) {
                    case NetworkJsonKeyDefine.TRAVEL:
                        switch (result) {
                            case NetworkJsonKeyDefine.RESULT_SUCCESS:
                                String travelName = originJSONObject.getJSONObject(NetworkJsonKeyDefine.DATA_KEY)
                                        .getString(NetworkJsonKeyDefine.NAME);
                                TabLayout.Tab tab = tabLayout.getTabAt(0);
                                tab.setText(travelName);
                                currentTravelId = originJSONObject.getJSONObject(NetworkJsonKeyDefine.DATA_KEY)
                                        .getInt(NetworkJsonKeyDefine.ID);
                                Log.v("ekuri", "on handle network event");
                                initdrawerAdapter();
                                listView_drawer.setAdapter(drawerAdapter);
                                Message message = new Message();
                                message.what = AMap_MySelf_Fragment.UPDATE;
                                Bundle bundle = new Bundle();
                                bundle.putDouble(LOCATION_LAT_KEY, locationLat);
                                bundle.putDouble(LOCATION_LNG_KEY, locationLng);
                                message.setData(bundle);
                                handler.sendMessage(message);
                                fab.setBackgroundResource(R.drawable.ic_note_add_black_24dp);

                                break;
                        }
                        break;
                }
                break;
            case NetworkJsonKeyDefine.QUERY_ALL:
                switch (target) {
                    case NetworkJsonKeyDefine.TRAVEL:
                        switch (result) {
                            case NetworkJsonKeyDefine.RESULT_SUCCESS:
                                JSONArray jsonArray = originJSONObject.
                                        getJSONArray(NetworkJsonKeyDefine.DATA_KEY);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    queryTravelItemCount = 0;
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    travelIdList.add(jsonObject.getInt(NetworkJsonKeyDefine.ID));
                                    nameList.add(jsonObject.getString(NetworkJsonKeyDefine.NAME));

                                    dataManager.queryTravelItemIdListByTravelId(travelIdList.get(i),
                                            networkHandler);
                                }
                                drawerAdapter = new DrawerAdapter(travelIdList, uriList, nameList, getApplicationContext(),
                                        this);
                                listView_drawer.setAdapter(drawerAdapter);
                                break;
                        }
                        break;
                    case NetworkJsonKeyDefine.TRAVEL_ITEM:
                        switch (result) {
                            case NetworkJsonKeyDefine.RESULT_SUCCESS:
                                JSONArray jsonArray = originJSONObject.getJSONArray(
                                        NetworkJsonKeyDefine.DATA_KEY
                                );
                                if (jsonArray.length() > 0) {
                                    TravelItem travelItem = TravelItem.fromJson(jsonArray.getString(0), true);
                                    int i = travelIdList.indexOf(travelItem.travelId);
                                    uriList.add(i, travelItem.media);
                                } else {
                                    uriList.add(null);
                                }
                                queryTravelItemCount++;
                                if (queryTravelItemCount == travelIdList.size()) {
                                    drawerAdapter = new DrawerAdapter(travelIdList, uriList, nameList, getApplicationContext(),
                                            this);
                                    listView_drawer.setAdapter(drawerAdapter);
                                }
                                break;
                        }
                        break;
                }
                break;
            case NetworkJsonKeyDefine.QUERY:
                switch (target) {
                    case NetworkJsonKeyDefine.TRAVEL_ITEM:
                        switch (result) {
                            case NetworkJsonKeyDefine.RESULT_SUCCESS:
                                break;
                        }
                        break;
                }
                break;
            case NetworkJsonKeyDefine.REMOVE:
                if (target.equals(NetworkJsonKeyDefine.TRAVEL) && result.equals(NetworkJsonKeyDefine.RESULT_SUCCESS)) {
                    Log.v("wilbert", "removeTravel successfully on net");
                    initdrawerAdapter();
                }
        }
    }
}
