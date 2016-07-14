package com.jiangzuomeng.travelmap;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jiangzuomeng.Adapter.SetTagAdapter;
import com.jiangzuomeng.dataManager.DataManager;
import com.jiangzuomeng.dataManager.NetworkConnectActivity;
import com.jiangzuomeng.dataManager.NetworkHandler;
import com.jiangzuomeng.modals.TravelItem;
import com.jiangzuomeng.networkManager.NetWorkManager;
import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class CreateNewItemActivity extends AppCompatActivity implements NetworkConnectActivity{
    private static final int CAMERA = 1000;
    private static final int PICK_IMAGE_REQUEST = 1234;
    private static  final int ID_START = 234;
    public static final int TRAVEL_ITEM_RESULT_SUCCESS_CODE = 4354;
    public static  final int TRAVEL_ITEM_RESULT_FAIL_CODE = 4355;

    public static final String IMAGEVIEW = "imageView";
    public static final String URISTRING = "uriString";
    int countStart = 0;
    File mediaStorageDir;
    ImageView image;
    View.OnClickListener onClickListener;
    AdapterView.OnItemClickListener onItemClickListener;
    ImageView.OnLongClickListener onLongClickListener;
    ListView.OnItemLongClickListener onItemLongClickListener;
    LinearLayout pictureLinearLayout;
    Uri fileUri;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    List<Integer> imageIds = new ArrayList<>();
    List<HashMap<String, Object>> imageIdStringList = new ArrayList<>();
    Button setTagBtn;
    PopupWindow setTagPopUpWindow;
    Button addOtherTagBtn;
    SetTagAdapter setTagAdapter;
    String tagTemp;
    EditText addTagEditText;
    ListView tagListView;
    EditText itemTextEditText;

    Double locationLng;
    Double locationLat;
    DataManager dataManager;
    List<String> imageStringList;
    List<String> labelStringList;

    boolean isCreateNewTravelItem = true;
    TravelItem currentTravelItem = new TravelItem();

    NetworkHandler networkHandler = new NetworkHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_item);
        init();

        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TravelMap");
        if (! mediaStorageDir.exists()){
            mediaStorageDir.mkdirs();
        }
    }

    private void init() {
        initEventListener();
        initTagAdapter();
        dataManager = DataManager.getInstance(getApplicationContext());
        imageStringList = new ArrayList<>();
        labelStringList = new ArrayList<>();

        image = (ImageView)findViewById(R.id.Select_image);
        image.setOnClickListener(onClickListener);
        pictureLinearLayout = (LinearLayout)findViewById(R.id.createItemPictureLinearLayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemTextEditText = (EditText)findViewById(R.id.ItemTexteditText);
        setTagBtn = (Button)findViewById(R.id.set_Tag_Btn);
        setTagBtn.setOnClickListener(onClickListener);

        currentTravelItem.id = getIntent().getIntExtra(SingleTravelActivity.INTENT_TRAVEL_KEY, -1);
        if (currentTravelItem.id != -1) {
            isCreateNewTravelItem = false;
            // TODO: 2015/12/14 network
            dataManager.queryTravelItemByTravelItemId(currentTravelItem.id, networkHandler);
            return;
        }

        Bundle bundle = getIntent().getExtras();
        locationLat = bundle.getDouble(MainActivity.LOCATION_LAT_KEY);
        locationLng = bundle.getDouble(MainActivity.LOCATION_LNG_KEY);
    }

    private void initTagAdapter() {
        List<String> strings = new ArrayList<>();
        if (isCreateNewTravelItem) {
            for (int i = 0; i < 5; i++)
                strings.add(Integer.toString(i));
            setTagAdapter = new SetTagAdapter(strings, this);
        } else {
        }
    }

    private void initEventListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Select_image:
                        showSelectionDialog();
                        break;
                    case R.id.set_Tag_Btn:
                        showSelectionTagPop(v);
                    case R.id.addOtherTagBtn:
                        addOtherTag(v);
                }
            }
        };


        onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("wilbert", "item click");
                switch (position) {
                    case 0:
                        //start camera
                        fileUri = getOutImageFileUri();
                        Log.v("wilbert", fileUri.toString());
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        startActivityForResult(intent, CAMERA);
                        dialog.dismiss();
                        break;
                    case 1:
                        //start choose
                        Intent chooseintent = new Intent();
                        chooseintent.setType("image/*");
                        chooseintent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(chooseintent, "choose a picture"), PICK_IMAGE_REQUEST);
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };
        onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                int delete = -1;
                for (int i = 0; i < imageIdStringList.size(); i++) {
                    hashMap = imageIdStringList.get(i);
                    ImageView imageView = (ImageView)hashMap.get(IMAGEVIEW);
                    if (imageView.getId() == v.getId()) {
                        delete = i;
                        break;
                    }
                }
                imageIdStringList.remove(delete);
                pictureLinearLayout.removeView(v);
                fileUri = null;
                return true;
            }
        };
        onItemLongClickListener = new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int delete = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewItemActivity.this);
                builder.setMessage("确认删除吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setTagAdapter.removeAt(delete);
                        Log.v("wilbert", "remove map strings");
                        setTagAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                //tagListView.setAdapter(setTagAdapter);
                return true;
            }
        };
    }

    private Uri getOutImageFileUri() {
        return Uri.fromFile(getOutputImageFile());
    }

    private File getOutputImageFile() {
        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
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

    private void addOtherTag(View v) {
        if(!addTagEditText.getText().toString().equals("")) {
            setTagAdapter.getStrings().add(addTagEditText.getText().toString());
            setTagAdapter.getIsSelectList().add(setTagAdapter.getStrings().size()-1, false);
            setTagAdapter.notifyDataSetChanged();
            addTagEditText.setText("");
        }
    }

    private void showSelectionTagPop(View v) {
        View popView = getLayoutInflater().inflate(R.layout.popup_set_tag, null);
        setTagPopUpWindow = new PopupWindow(popView, ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, true);
        setTagPopUpWindow.setTouchable(true);
        setTagPopUpWindow.setOutsideTouchable(true);
        setTagPopUpWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        tagListView = (ListView)popView.findViewById(R.id.tag_listView);
        addOtherTagBtn = (Button)popView.findViewById(R.id.addOtherTagBtn);
        addOtherTagBtn.setOnClickListener(onClickListener);
        addTagEditText = (EditText)popView.findViewById(R.id.AddTagEditText);
        tagListView.setAdapter(setTagAdapter);
        tagListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SetTagAdapter.ViewHolder viewHolder = (SetTagAdapter.ViewHolder) view.getTag();
                viewHolder.checkBox.toggle();
                setTagAdapter.getIsSelectList().set(position, viewHolder.checkBox.isChecked());
            }
        });
        tagListView.setOnItemLongClickListener(onItemLongClickListener);
        setTagPopUpWindow.showAsDropDown(v);
    }


    private void showSelectionDialog() {
        View popView= getLayoutInflater().inflate(R.layout.dialog_camera_select,null);

        builder = new AlertDialog.Builder(this);
        ListView listView = (ListView)popView.findViewById(R.id.camera_select_listview);
        builder.setView(popView);
        String string[] = new String[] {"拍照", "选择照片"};
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, string));
        listView.setOnItemClickListener(onItemClickListener);
        dialog = builder.show();
    }

    public String getRealFilePath(final Uri targetFileUri) {
        final String scheme = targetFileUri.getScheme();
        if (null == scheme || ContentResolver.SCHEME_FILE.equals(scheme)) {
            return targetFileUri.getPath();
        }
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getApplicationContext().getContentResolver().query(
                    targetFileUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);

            if (null == cursor)
                return null;
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(index);
            cursor.close();
            return result;
        }

        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_confirm:
                String editText = itemTextEditText.getText().toString();
                String imageString = null;
                if (imageIdStringList.size() > 0) {
                    HashMap<String , Object> hashMap = imageIdStringList.get(0);
                    imageString = (String)hashMap.get(URISTRING);
                }

                labelStringList = getLabelStringList();
                String labelString = null;
                if (labelStringList.size() > 0)
                    labelString = labelStringList.get(0);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                currentTravelItem.text = editText;
                currentTravelItem.label = labelString;
                currentTravelItem.locationLat = locationLat;
                currentTravelItem.locationLng = locationLng;
                currentTravelItem.time = timeStamp;

                try {
                    if (null == fileUri) {
                        currentTravelItem.media = null;
                    } else {
                        File targetFile = dataManager.moveAndRenameFile(getContentResolver().openInputStream(fileUri));
                        dataManager.uploadFile(targetFile, networkHandler);
                        currentTravelItem.media = targetFile.getName();
                    }

                    if (isCreateNewTravelItem) {
                        currentTravelItem.travelId = MainActivity.currentTravelId;
                        dataManager.addNewTravelItem(currentTravelItem, networkHandler);
                    } else {
                        dataManager.updateTravelItem(currentTravelItem, networkHandler);
                    }
                } catch (NoSuchAlgorithmException | IOException e) {
                    e.printStackTrace();
                }
                finish();
                break;
        }
        return true;
    }

    private List<String> getLabelStringList() {
        List<String> tagString = new ArrayList<>();
        for (int i = 0; i < setTagAdapter.getIsSelectList().size() && i < setTagAdapter
                .getStrings().size(); i++) {
            if (setTagAdapter.getIsSelectList().get(i)) {
                tagString.add(setTagAdapter.getStrings().get(i));
            }
        }
        return tagString;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_new_item, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode  == CAMERA) {
            if(resultCode == RESULT_OK) {
                try {
                    File newFile = dataManager.renameFile(new File(fileUri.getPath()));
                    addImageFromImageName(newFile.getName());
                } catch (NoSuchAlgorithmException | IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            fileUri = data.getData();
        }

        addImageFromImageName();
    }

    private void addImageFromImageName() {
        try {
            float px = 90 * (getResources().getDisplayMetrics().densityDpi / 160f);
            Bitmap bitmap = dataManager.getBitmapFromUri(fileUri, 90);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmap);
            imageView.setId(ID_START + countStart);
            imageView.setPadding(10, 10, 10, 10);
            imageView.setLayoutParams(new LinearLayout.LayoutParams((int) px, (int) px));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(IMAGEVIEW, imageView);
            String uriString = fileUri.toString();
            String fileName = uriString.substring(uriString.lastIndexOf(File.separator)+1,
                    uriString.length());
            hashMap.put(URISTRING, fileName);
            imageIdStringList.add(hashMap);
            countStart++;
            imageView.setOnLongClickListener(onLongClickListener);
            pictureLinearLayout.addView(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleNetworkEvent(String result, String request, String target, JSONObject originJSONObject) throws JSONException {
        switch (request) {
            case NetworkJsonKeyDefine.ADD:
                switch (result) {
                    case NetworkJsonKeyDefine.RESULT_SUCCESS:
                        Log.v("wilbert", originJSONObject.toString());
                        break;
                }
                break;
            case NetworkJsonKeyDefine.UPDATE:
                switch (result) {
                    case NetworkJsonKeyDefine.RESULT_SUCCESS:
                        Log.v("wilbert", originJSONObject.toString());
                }
                break;
            case NetworkJsonKeyDefine.QUERY:
                if (target.equals(NetworkJsonKeyDefine.TRAVEL_ITEM) && result.equals(
                        NetworkJsonKeyDefine.RESULT_SUCCESS)) {
                    String jsonString = originJSONObject.getString(NetworkJsonKeyDefine.DATA_KEY);
                    currentTravelItem = TravelItem.fromJson(jsonString, true);
                    if (currentTravelItem.media != null) {
                        fileUri = getUriFromImageName(currentTravelItem.media);
                        addImageFromImageName();
                    }
                    itemTextEditText.setText(currentTravelItem.text);
                    locationLat = currentTravelItem.locationLat;
                    locationLng = currentTravelItem.locationLng;
                    List<String> strings = new ArrayList<>();
                    strings.add(currentTravelItem.label);
                    setTagAdapter = new SetTagAdapter(strings, this);
                    setTagAdapter.getIsSelectList().set(0, true);
                }
        }
    }

    public Uri getUriFromImageName(String imageName) {
        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TravelMap");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        File file = new File(mediaStorageDir.getPath()+File.separator+imageName);
        return Uri.fromFile(file);
    }
}
