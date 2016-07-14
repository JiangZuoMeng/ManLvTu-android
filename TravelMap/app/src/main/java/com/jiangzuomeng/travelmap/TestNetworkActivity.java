package com.jiangzuomeng.travelmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jiangzuomeng.dataManager.DataManager;
import com.jiangzuomeng.modals.TravelItem;
import com.jiangzuomeng.networkManager.NetWorkManager;
import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestNetworkActivity extends AppCompatActivity {
    public static final int UPDATE = 121323;
    public static final String UPDATESTRING = "updateString";
    public static final int PICK_IMAGE_REQUEST = 3452;
    public static final int CAMERA = 3431;
    Button button;
    TextView textView;
    NetWorkManager netWorkManager;
    Button button2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE:
                    Bundle bundle = msg.getData();
                    String string = bundle.getString(UPDATESTRING);
                    textView.setText(string);
                    break;
                case NetworkJsonKeyDefine.NETWORK_OPERATION:
                    Bundle bundle1 = msg.getData();
                    String s = bundle1.getString(NetworkJsonKeyDefine.NETWORK_RESULT_KEY);
                    Log.v("wilbert", s);
            }
        }
    };
    DataManager dataManager;
    Uri fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_network);

        button = (Button)findViewById(R.id.testNetworkbutton);
        button = (Button)findViewById(R.id.testNetworkbutton2);
        textView = (TextView)findViewById(R.id.testNetworktextView);
        netWorkManager = new NetWorkManager();
        dataManager = DataManager.getInstance(getApplicationContext());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelItem travelItem = new TravelItem();
                travelItem.travelId = 12;
                travelItem.label = "asdf";
                travelItem.time = "sdf";
                travelItem.locationLat = 23;
                travelItem.locationLng = 121;
                travelItem.like = 0;
                travelItem.text = "text";
                travelItem.media = "sada";

                Intent chooseintent = new Intent();
                chooseintent.setType("image/*");
                chooseintent.setAction(Intent.ACTION_GET_CONTENT);
                Uri uri = getOutImageFileUri();
                Log.v("wilbert", "uri:" + uri.toString());
                startActivityForResult(Intent.createChooser(chooseintent, "choose a picture"), PICK_IMAGE_REQUEST);

            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
        Uri uri = data.getData();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority("192.168.191.1:3030")
                .appendEncodedPath("id")
                .appendEncodedPath(uri.getEncodedPath());
        Log.v("wilbert", builder.build().toString());
        Log.v("wilbert", uri.toString());
    }
    }

    private Uri getOutImageFileUri() {
        return Uri.fromFile(getOutputImageFile());
    }

    private File getOutputImageFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TravelMap");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
        return mediaFile;
    }

}
