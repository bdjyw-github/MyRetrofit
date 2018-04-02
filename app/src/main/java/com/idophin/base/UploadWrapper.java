package com.idophin.base;

import android.util.Log;

import com.idophin.entity.PariKeyValue;
import com.idophin.task.callback.TastListener;
import com.idophin.util.Constants;
import com.idophin.util.JSONApi;
import com.idophin.util.RSAUtils;
import com.idophin.util.SHA1Utils;
import com.idophin.util.SignatureUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liyanchuan on 2018/3/20.
 */

public class UploadWrapper {
    public static final String MULTIPART_FORM_DATA = "image/*";       // 指明要上传的文件格式

    public static void okHttpUpload(String partName, String path, final UploadCallback callback) {
        File file = new File(path);             // 需要上传的文件
        RequestBody requestFile =               // 根据文件格式封装文件
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // 初始化请求体对象，设置Content-Type以及文件数据流
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)            // multipart/form-data
                .addFormDataPart(partName, file.getName(), requestFile)
                .build();

        // 封装OkHttp请求对象，初始化请求参数
        Request request = new Request.Builder()
                .url("http://dev.byhsl.tmlsoft.com:81/api/Upload")                // 上传url地址
                .post(requestBody)              // post请求体
                .build();

        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                .connectTimeout(100, TimeUnit.SECONDS)          // 设置请求超时时间
                .writeTimeout(150, TimeUnit.SECONDS)
                .build();
        // 发起异步网络请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(call, e);
                }
            }
        });
    }

    class UploadCallback {

        public void onResponse(Call call, Response response) {
        }

        public void onFailure(Call arg0, IOException e) {
        }
    }

    public static void uploadFile(ArrayList<String> pathes, final TastListener listener) {
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        String mRanStr = SignatureUtil.getRandomString(8);
        String mToken = BaseApplication.getInstance().getToken() != null ? BaseApplication.getInstance().getToken() : "12121212";
        ArrayList<PariKeyValue> entryList = new ArrayList<>();

        try {
            if (pathes != null && pathes.size() > 0) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < pathes.size(); i++) {
                    File file = new File(pathes.get(i));
                    String name = file.getName();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("fileName", pathes.get(i));
                    jsonObject.put("filekey", name.substring(0, name.lastIndexOf(".") - 1));
                    jsonArray.put(jsonObject);
                    //body.put(name.substring(0, name.lastIndexOf(".")), file);

                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    requestBodyBuilder.addFormDataPart(name.substring(0, name.lastIndexOf(".") - 1), pathes.get(i), requestFile);
                }

                // body.put("UPLOAD",jsonArray.toString());

                Log.e("OkHttp", "uploadFile upLoadInfo =  " + jsonArray);

                requestBodyBuilder.addFormDataPart("upLoadInfo", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
                requestBodyBuilder.addFormDataPart("createid", "" + BaseApplication.getInstance().getUser().getUserId());
                requestBodyBuilder.addFormDataPart("companykey", "0");

                entryList.add(new PariKeyValue("upLoadInfo", URLEncoder.encode(jsonArray.toString(), "UTF-8")));
                entryList.add(new PariKeyValue("createid", "" + BaseApplication.getInstance().getUser().getUserId()));
                entryList.add(new PariKeyValue("companykey", "0"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String crypt = SHA1Utils.getSign(entryList, mRanStr, mToken);
        PublicKey publicKey = null;
        try {
            publicKey = RSAUtils.loadPublicKey(Constants.Service_PublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String RasToken = RSAUtils.encryptData(mToken.getBytes(), publicKey);
        String RasKey = RSAUtils.encryptData(mRanStr.getBytes(), publicKey);

        RequestBody multipartBody = requestBodyBuilder.build();
        Request request = new Request.Builder().url("http://dev.byhsl.tmlsoft.com:81/api/Upload")
                .addHeader("User-Agent", "android")
                .addHeader("crypt", crypt)
                .addHeader("token", RasToken)
                .addHeader("key", RasKey)
                .addHeader("version", Constants.URL_V1)
                .addHeader("author", Constants.Service_Key)
                .header("Content-Type", "text/html; charset=utf-8;")
                .post(multipartBody)//传参数、文件或者混合，改一下就行请求体就行
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TEST", "onFailure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TEST", "onResponse");
                String js = response.body().string();
                JSONObject jsonobj = null;
                try {
                    jsonobj = new JSONObject(js);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("TEST", "onResponse jsonobj = " + jsonobj.toString());
                if (JSONApi.Analysis(jsonobj)) {//解析逻辑数据
                    try {
                        jsonobj = jsonobj.getJSONObject("data");
                        if (listener != null) {
                            listener.onResponse(jsonobj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String respMsg = jsonobj.getString("msg");
                        if (listener != null) {
                            listener.onResponseErro(jsonobj.getInt("code"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
