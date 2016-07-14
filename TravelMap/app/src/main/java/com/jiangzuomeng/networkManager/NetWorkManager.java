package com.jiangzuomeng.networkManager;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by wilbert on 2015/11/22.
 */
public class NetWorkManager {
    public String getDataFromUrl(URL url) throws IOException {
        Log.v("ekuri", "openning url:" + url.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        InputStream inputStream = urlConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        return  stringBuilder.toString();
    }

    public InputStream downloadFile(String filename) throws IOException {
        Log.v("ekuri", "downloading file: " + filename);

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.FILE_DOWNLOAD_FREFIX)
                .appendQueryParameter(NetworkJsonKeyDefine.FILENAME, filename);

        URL url = new URL(uriBuilder.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        if (urlConnection.getResponseCode() == NetworkJsonKeyDefine.RESULT_NOT_FOUND) {
            return null;
        }
        return urlConnection.getInputStream();
    }

    public String postFile(File targetFile) throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String SPACER = "--", LINE_END = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(NetworkJsonKeyDefine.HTTP)
                .encodedAuthority(NetworkJsonKeyDefine.host)
                .appendPath(NetworkJsonKeyDefine.FILE_UPLOAD_PREFIX);
        URL url = new URL(uriBuilder.toString());
        Log.v("ekuri", "openning url:" + url.toString());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setReadTimeout(5 * 1000);
        httpURLConnection.setDoInput(true);// 允许输入
        httpURLConnection.setDoOutput(true);// 允许输出
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestMethod("POST"); // Post方式
        httpURLConnection.setRequestProperty("connection", "keep-alive");
        httpURLConnection.setRequestProperty("Charset", "UTF-8");
        httpURLConnection.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);

        // 首先组拼文本类型的参数
        StringBuilder filePropertyStringBuilder = new StringBuilder();
        filePropertyStringBuilder.append(SPACER);
        filePropertyStringBuilder.append(BOUNDARY);
        filePropertyStringBuilder.append(LINE_END);
        filePropertyStringBuilder.append("Content-Disposition: form-data; name=\""
                + NetworkJsonKeyDefine.FILE_UPLOAD_PARAMETER + "\"" + LINE_END);
        filePropertyStringBuilder.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END);
        filePropertyStringBuilder.append("Content-Transfer-Encoding: 8bit" + LINE_END);
        filePropertyStringBuilder.append(LINE_END);
        filePropertyStringBuilder.append(targetFile.getName());
        filePropertyStringBuilder.append(LINE_END);

        DataOutputStream outStream = new DataOutputStream(httpURLConnection
                .getOutputStream());
        outStream.write(filePropertyStringBuilder.toString().getBytes());

        // 发送文件数据
        StringBuilder fileDataStringBuilder = new StringBuilder();
        fileDataStringBuilder.append(SPACER);
        fileDataStringBuilder.append(BOUNDARY);
        fileDataStringBuilder.append(LINE_END);
        fileDataStringBuilder
                .append("Content-Disposition: form-data; name=\"file\"; filename=\""
                        + targetFile.getName() + "\"" + LINE_END);
        fileDataStringBuilder.append("Content-Type: application/octet-stream; charset="
                + CHARSET + LINE_END);
        fileDataStringBuilder.append(LINE_END);
        outStream.write(fileDataStringBuilder.toString().getBytes());
        InputStream fileInputStream = new FileInputStream(targetFile);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fileInputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }

        fileInputStream.close();
        outStream.write(LINE_END.getBytes());

        // 请求结束标志
        byte[] end_data = (SPACER + BOUNDARY + SPACER + LINE_END).getBytes();
        outStream.write(end_data);
        outStream.flush();

        // 得到响应码
        int res = httpURLConnection.getResponseCode();
        InputStream inputStream = httpURLConnection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(inputLine);
        }

        outStream.close();
        httpURLConnection.disconnect();
        return stringBuilder.toString();
    }
}
