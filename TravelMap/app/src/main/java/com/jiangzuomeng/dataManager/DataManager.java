package com.jiangzuomeng.dataManager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

import com.amap.api.maps2d.model.LatLng;
import com.jiangzuomeng.database.DBManager;
import com.jiangzuomeng.modals.Comment;
import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;
import com.jiangzuomeng.modals.Travel;
import com.jiangzuomeng.modals.TravelItem;
import com.jiangzuomeng.modals.User;
import com.jiangzuomeng.networkManager.NetWorkManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Lock;

/**
 * Created by wilbert on 2015/11/22.
 */
public class DataManager {
    private static DataManager dataManager = null;
    private DBManager dbManager;
    private NetWorkManager netWorkManager;
    private Context context;

    public static DataManager getInstance(Context context) {
        if (dataManager == null) {
            dataManager = new DataManager(context);
        }
        return dataManager;
    }

    private DataManager(Context context) {
        dbManager = new DBManager(context);
        netWorkManager = new NetWorkManager();
        this.context = context;
    }

    public void login(User user, NetworkHandler handler) {
        try {
            runThreadByUrl(user.getLoginUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void registerUser(User user, Handler handler) {
        try {
            runThreadByUrl(user.getAddUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void queryUserByUserId(int id, Handler handler) {
        User user = new User();
        user.id = id;
        try {
            runThreadByUrl(user.getQueryUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user, Handler handler) {
        try {
            runThreadByUrl(user.getUpdateUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserByUserId(int userId, Handler handler) {
        User user = new User();
        user.id = userId;
        try {
            runThreadByUrl(user.getRemoveUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void addNewTravel(Travel travel, Handler handler) {
        try {
            runThreadByUrl(travel.getAddUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void queryTravelByTravelId(int travelId, Handler handler) {
        Travel travel = new Travel();
        travel.id = travelId;
        try {
            runThreadByUrl(travel.getQueryUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void removeTravelByTravelId(int travelId, Handler handler) {
        Travel travel = new Travel();
        travel.id = travelId;
        try {
            runThreadByUrl(travel.getRemoveUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void updateTravel(Travel travel, Handler handler) {
        try {
            runThreadByUrl(travel.getUpdateUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void queryTravelIdListByUserId(int userId, Handler handler) {
        Travel travel = new Travel();
        travel.userId = userId;
        try {
            runThreadByUrl(travel.getQueryAllUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void addNewTravelItem(TravelItem travelItem, Handler handler) {
        try {
            runThreadByUrl(travelItem.getAddUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void queryTravelItemByTravelItemId(int travelItemId, Handler handler) {
        TravelItem travelItem = new TravelItem();
        travelItem.id = travelItemId;
        try {
            runThreadByUrl(travelItem.getQueryUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void queryNearbyTravelItem(LatLng currentLocation, Handler handler) {
        if (null == currentLocation) {
            return;
        }
        double nearDistance = NetworkJsonKeyDefine.NEAR_DISTANCE;
        try {
            runThreadByUrl(TravelItem.getQueryNearbyUrl(currentLocation.latitude - nearDistance,
                    currentLocation.latitude + nearDistance,
                    currentLocation.longitude - nearDistance,
                    currentLocation.longitude + nearDistance), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void queryTravelItemIdListByTravelId(int travelId, Handler handler) {
        TravelItem travelItem = new TravelItem();
        travelItem.travelId = travelId;
        try {
            runThreadByUrl(travelItem.getQueryAllUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void removeTravelItemByTravelItemId(int travelItemId, Handler handler) {
        TravelItem travelItem = new TravelItem();
        travelItem.id = travelItemId;
        try {
            runThreadByUrl(travelItem.getRemoveUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void updateTravelItem(TravelItem travelItem, Handler handler) {
        try {
            runThreadByUrl(travelItem.getUpdateUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void addNewComment(Comment comment, Handler handler) {
        try {
            runThreadByUrl(comment.getAddUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void queryCommentByCommentId(int commentid, Handler handler) {
        Comment comment = new Comment();
        comment.id = commentid;
        try {
            runThreadByUrl(comment.getQueryUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void queryCommentIdListByTravelItemId(int travelItemid, Handler handler) {
        Comment comment = new Comment();
        comment.travelItemId = travelItemid;
        try {
            runThreadByUrl(comment.getQueryAllUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void removeCommentByCommentId(int commentId, Handler handler) {
        Comment comment = new Comment();
        comment.id = commentId;
        try {
            runThreadByUrl(comment.getRemoveUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void updateComment(Comment comment, Handler handler) {
        try {
            runThreadByUrl(comment.getUpdateUrl(), handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void runThreadByUrl(final URL url, final Handler handler) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String dataString = netWorkManager.getDataFromUrl(url);
                    Message message = new Message();
                    message.what = NetworkJsonKeyDefine.NETWORK_OPERATION;
                    Bundle bundle = new Bundle();
                    bundle.putString(NetworkJsonKeyDefine.NETWORK_RESULT_KEY, dataString);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void uploadFile(final File targetFile, final Handler handler) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // check whether the file having exists in server
                    Uri.Builder uriBuilder = new Uri.Builder();
                    uriBuilder.scheme(NetworkJsonKeyDefine.HTTP)
                            .encodedAuthority(NetworkJsonKeyDefine.host)
                            .appendPath(NetworkJsonKeyDefine.FILE_UPLOAD_PREFIX)
                            .appendQueryParameter(NetworkJsonKeyDefine.FILENAME, targetFile.getName());
                    String fileCheckResult = netWorkManager.getDataFromUrl(new URL(uriBuilder.toString()));

                    JSONTokener parser = new JSONTokener(fileCheckResult);

                    try {
                        String dataString;
                        JSONObject jsonObject = (JSONObject) parser.nextValue();
                        if (jsonObject.getString(NetworkJsonKeyDefine.RESULT_KEY).equals(NetworkJsonKeyDefine.RESULT_FAILED)) {
                            dataString = fileCheckResult;
                            Log.v("ekuri", "file already exists in server, will not upload");
                        } else {
                            // upload file if not exist
                            dataString = netWorkManager.postFile(targetFile);
                        }

                        Message message = new Message();
                        message.what = NetworkJsonKeyDefine.NETWORK_OPERATION;
                        Bundle bundle = new Bundle();
                        bundle.putString(NetworkJsonKeyDefine.NETWORK_RESULT_KEY, dataString);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private LinkedBlockingDeque<String> fileDownloadQueue = new LinkedBlockingDeque<>();
    private Handler fileDownloadCurrentHandler;
    public void downLoadFile(final String filename, final Handler handler) {
        fileDownloadCurrentHandler = handler;

        if (fileDownloadQueue.contains(filename)) {
            Log.v("ekuri", "file already in download queue: " + filename);
        }
        if (!fileDownloadQueue.offer(filename)) {
            Log.v("ekuri", "can not add filename to download queue: " + filename);
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!fileDownloadQueue.isEmpty()) {
                    try {
                        String filename = fileDownloadQueue.take();

                        JSONObject resultJson = new JSONObject();
                        resultJson.put(NetworkJsonKeyDefine.REQUEST_KEY, NetworkJsonKeyDefine.FILE_DOWNLOAD);
                        resultJson.put(NetworkJsonKeyDefine.TARGET_KEY, NetworkJsonKeyDefine.FILE);

                        if (getLocalFile(filename).exists()) {
                            Log.v("ekuri", "file already exists: " + filename);
                            resultJson.put(NetworkJsonKeyDefine.RESULT_KEY, NetworkJsonKeyDefine.RESULT_SUCCESS);
                            resultJson.put(NetworkJsonKeyDefine.DATA_KEY, filename);
                        } else {
                            InputStream fileInputStream = netWorkManager.downloadFile(filename);
                            if (null == fileInputStream) {
                                resultJson.put(NetworkJsonKeyDefine.RESULT_KEY, NetworkJsonKeyDefine.RESULT_FAILED);
                            } else {
                                File resultFile = moveAndRenameFile(fileInputStream);
                                resultJson.put(NetworkJsonKeyDefine.RESULT_KEY, NetworkJsonKeyDefine.RESULT_SUCCESS);
                                resultJson.put(NetworkJsonKeyDefine.DATA_KEY, resultFile.getName());
                            }
                        }


                        Message message = new Message();
                        message.what = NetworkJsonKeyDefine.NETWORK_OPERATION;
                        Bundle bundle = new Bundle();
                        bundle.putString(NetworkJsonKeyDefine.NETWORK_RESULT_KEY, resultJson.toString());
                        message.setData(bundle);
                        fileDownloadCurrentHandler.sendMessage(message);
                    } catch (IOException | NoSuchAlgorithmException | JSONException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    public File renameFile(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[8196];
        int read;
        while ((read = inputStream.read(buffer)) > 0) {
            messageDigest.update(buffer, 0, read);
        }
        byte[] md5sum = messageDigest.digest();
        BigInteger bigInt = new BigInteger(1, md5sum);
        String output = bigInt.toString(16);
        // Fill to 32 chars
        output = String.format("%32s", output).replace(' ', '0');
        String path = file.getPath();
        String newPath = path.substring(0, path.lastIndexOf(File.separator) + 1);
        File newFile = new File(newPath + output + ".jpg");
        file.renameTo(newFile);
        return newFile;
    }

    /*
    * @param path
    * the path of file:  use getPath() method.
    * File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TravelMap");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

    * */
    public File moveAndRenameFile(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
        File newFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + "TravelMap" + File.separator + "temp.jpg");

        OutputStream outputStream = new FileOutputStream(newFile);
        byte[] buffer = new byte[1024];

        int length;
        //copy the file content in bytes
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();

        return renameFile(newFile);
    }

    public Bitmap getBitmapFromUri(Uri uri, float heightPx) throws FileNotFoundException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
//                    options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        float bitmapHeight = options.outHeight;
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float px = heightPx * (displayMetrics.densityDpi / 160f);
        int sampleSize = (int) (bitmapHeight / px);
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        inputStream = context.getContentResolver().openInputStream(uri);
        bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        return bitmap;
    }

    public static File getLocalFile(String filename) {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + "TravelMap" + File.separator + filename);
    }
    public static Uri getUriFromImageName(String imageName) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
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
