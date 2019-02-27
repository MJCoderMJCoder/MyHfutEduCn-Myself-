package com.lzf.myhfuteducn.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 处理网络请求的工具类
 *
 * @author MJCoder
 */
public class OkHttpUtil {
    /**
     * @see OkHttpClient
     */
    private static OkHttpClient client = new OkHttpClient();

    /**
     * 用于进行POST请求
     *
     * @param url    POST请求的URL
     * @param params POST请求的一系列纯文本
     * @param files  POST请求的一系列文件
     * @return 服务端返回的响应信息
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String uploadFiles(String url, Map<String, String> params, Map<String, File> files) {
        //        Log.v("请求", url + "\n" + params.toString() + "\n" + files.toString());
        String message = "连接不到服务器，请检查你的网络或稍后重试。"; // 连接不到服务器，请检查你的网络或稍后重试
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // 设置类型
        builder.setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        if (files != null && files.size() > 0) {
            //            int i = 0;
            for (Map.Entry<String, File> entry : files.entrySet()) {
                //                i++;
                File temp = entry.getValue();
                //                builder.addFormDataPart("images" + i, temp.getName(), RequestBody.create(null, temp));
                builder.addFormDataPart(entry.getKey(), temp.getName(), RequestBody.create(null, temp));
            }
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            message = response.body().string();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //        Log.v("响应", url + "\n" + message);
        return message;
    }

    /**
     * 用于进行GET请求
     *
     * @param url GET请求的URL
     * @return 服务端返回的响应信息
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getData(String url) {
        //        Log.v("请求", url);
        String message = "连接不到服务器，请检查你的网络或稍后重试。"; // 连接不到服务器，请检查你的网络或稍后重试
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            message = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        Log.v("响应", url + "\n" + message);
        return message;
    }

    /**
     * 用于进行POST请求
     *
     * @param url    POST请求的URL
     * @param params POST请求的一系列纯文本
     * @return 服务端返回的响应信息
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String submit(String url, Map<String, String> params) {
        //        Log.v("请求", url + "\n" + params.toString());
        String message = "连接不到服务器，请检查你的网络或稍后重试。"; // 连接不到服务器，请检查你的网络或稍后重试
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            message = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        Log.v("响应", url + "\n" + message);
        return message;
    }
}
