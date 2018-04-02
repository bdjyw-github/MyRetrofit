package com.idophin.task;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.idophin.base.BaseApplication;
import com.idophin.entity.PariKeyValue;
import com.idophin.entity.UploadFile;
import com.idophin.task.callback.TastListener;
import com.idophin.task.converter.JsonConverterFactory;
import com.idophin.task.interceptor.BaseInterceptor;
import com.idophin.task.interceptor.CaheInterceptor;
import com.idophin.task.interceptor.HttpLoggingInterceptorM;
import com.idophin.task.interceptor.LogInterceptor;
import com.idophin.task.interceptor.OfflineCacheInterceptor;
import com.idophin.task.interceptor.UserAgentInterceptor;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liyanchuan on 2018/3/5.
 */

public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 20;
    private BaseApiService apiService;
    private static OkHttpClient okHttpClient;
    public static String baseUrl = BaseApiService.Base_URL;
    private static Context mContext;
    private static RetrofitClient sNewInstance;

    private static Retrofit retrofit;
    private Cache cache = null;
    private File httpCacheDirectory;


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(baseUrl);
    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);


    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(mContext);
        private static RetrofitClient NOIN_INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

//    public static RetrofitClient getNoIntercepterInstance(Context context) {
//        if (context != null) {
//            mContext = context;
//        }
//        return SingletonHolder.NOIN_INSTANCE;
//    }


    public static RetrofitClient getInstance(Context context, String url) {
        if (context != null) {
            mContext = context;
        }

        return new RetrofitClient(context, url);
    }

    public static RetrofitClient getInstance(Context context, String url, Map<String, String> headers) {
        if (context != null) {
            mContext = context;
        }
        return new RetrofitClient(context, url, headers);
    }

    private RetrofitClient() {
        Log.e("TEST", "RetrofitClient11111111111111111");
    }

    private RetrofitClient(Context context) {
        this(context, baseUrl, null);
    }

    private RetrofitClient(Context context, String url) {
        this(context, url, null);
    }

    private RetrofitClient(Context context, String url, Map<String, String> headers) {
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "tamic_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            }
        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }

        HttpLoggingInterceptorM logInter = new HttpLoggingInterceptorM(new LogInterceptor());
        logInter.setLevel(HttpLoggingInterceptorM.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(new CaheInterceptor(context))
                .addInterceptor(new UserAgentInterceptor("Android Device"))
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(logInter)
                .addInterceptor(new OfflineCacheInterceptor(context))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .build();

        Gson gsonDateFormat = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                //增加返回值为String的支持
                //.addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                //.addConverterFactory(GsonConverterFactory.create(gsonDateFormat))
                //增加返回值为Json的支持(以实体类返回)
                .addConverterFactory(JsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();

    }

    /**
     * ApiBaseUrl
     *
     * @param newApiBaseUrl
     */
    public static void changeApiBaseUrl(String newApiBaseUrl) {
        baseUrl = newApiBaseUrl;
        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl);
    }


    /**
     * ApiBaseUrl
     *
     * @param newApiHeaders
     */
    public static void changeApiHeader(Map<String, String> newApiHeaders) {

        okHttpClient.newBuilder().addInterceptor(new BaseInterceptor(newApiHeaders)).build();
        builder.client(httpClient.build()).build();
    }

    /**
     * create BaseApi  defalte ApiManager
     *
     * @return ApiManager
     */
    public RetrofitClient createBaseApi() {
        apiService = create(BaseApiService.class);
        return this;
    }

    public RetrofitClient createFileBaseApi() {

        HttpLoggingInterceptorM logInter = new HttpLoggingInterceptorM(new LogInterceptor());
        logInter.setLevel(HttpLoggingInterceptorM.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logInter)
                .build();
        apiService = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build().create(BaseApiService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }


    public Subscription get(String url, Map<String, Object> parameters, BaseSubscriber subscriber) {
        return apiService.executeGet(url, parameters)
                .compose(schedulersTransformer())
                .compose(transformerData())
                .subscribe(subscriber);
    }

    public void post(String url, Map<String, Object> parameters, BaseSubscriber subscriber) {
        apiService.executePost(url, parameters)
                .compose(schedulersTransformer())
                .compose(transformerData())
                .subscribe(subscriber);
    }

    public void postWithFile(String url, Map<String, Object> parameters, ArrayList<UploadFile> files, String fieldname, BaseSubscriber subscriber) {
        try {
            if (files != null && files.size() > 0) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < files.size(); i++) {
                    files.get(i).fieldname = fieldname;

                    JSONObject jsonObject = new JSONObject();
                    //jsonObject.put("fileName", pathes.get(i));
                    //jsonObject.put("filekey", name.substring(0, name.lastIndexOf(".") - 1));
                    jsonObject.put("uploadpath", files.get(i).uploadpath);
                    jsonObject.put("filename", files.get(i).filename);
                    jsonObject.put("fieldname", files.get(i).fieldname);
                    jsonArray.put(jsonObject);
                }

                Log.e("OkHttp", "postWithFile swf_file = " + jsonArray);

                parameters.put("swf_file", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
                parameters.put("createid", "" + BaseApplication.getInstance().getUser().getUserId());
                parameters.put("companykey", "0");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        apiService.executePost(url, parameters)
                .compose(schedulersTransformer())
                .compose(transformerData())
                .subscribe(subscriber);
    }

    public Subscription postJson(String url, Map<String, Object> parameters, Subscriber<JSONObject> subscriber) {
        RequestBody body = null;
        if (parameters != null) {
            body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), new Gson().toJson(parameters));
        }
        return apiService.executePostJson(url, body)
                .compose(schedulersTransformer())
                .compose(transformer())
                .subscribe(subscriber);
    }

    public void put(String url, Map<String, Object> parameters, BaseSubscriber subscriber) {
        apiService.executePut(url, parameters)
                .compose(schedulersTransformer())
                .compose(transformerData())
                .subscribe(subscriber);
    }

    public void delete(String url, Map<String, Object> parameters, BaseSubscriber subscriber) {
        apiService.executeDelete(url, parameters)
                .compose(schedulersTransformer())
                .compose(transformerData())
                .subscribe(subscriber);
    }


    public void upLoadFiles(ArrayList<String> pathes,BaseSubscriber subscriber) {
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

                //requestBodyBuilder.addFormDataPart("upLoadInfo", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
                requestBodyBuilder.addFormDataPart("upLoadInfo", jsonArray.toString());
                requestBodyBuilder.addFormDataPart("createid", "" + BaseApplication.getInstance().getUser().getUserId());
                requestBodyBuilder.addFormDataPart("companykey", "0");

                entryList.add(new PariKeyValue("upLoadInfo", URLEncoder.encode(jsonArray.toString(), "UTF-8")));
                //entryList.add(new PariKeyValue("upLoadInfo", jsonArray.toString()));
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

        Map<String ,Object> headers = new HashMap<>();
        headers.put("User-Agent", "android");
        headers.put("crypt", crypt);
        headers.put("token", RasToken);
        headers.put("key", RasKey);
        headers.put("version", Constants.URL_V1);
        headers.put("author", Constants.Service_Key);
        headers.put("Content-Type", "text/html; charset=utf-8;");

        MultipartBody multipartBody = requestBodyBuilder.build();

        apiService.uploadFiles("Upload", headers,multipartBody)
                .compose(schedulersTransformer())
                .compose(transformerData())
                .subscribe(subscriber);
    }

    public void uploadFile(ArrayList<String> pathes, final TastListener listener) {
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


    public void download(String url, final CallBack callBack) {
        apiService.downloadFile(url)
                .compose(schedulersTransformer())
                .compose(transformer())
                .subscribe(new DownSubscriber<ResponseBody>(callBack));
    }

    Observable.Transformer schedulersTransformer() {
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable) observable).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }

           /* @Override
            public Observable call(Observable observable) {
                return observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }*/
        };
    }

    <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer();
    }

    public <T> Observable.Transformer<BaseResponse<T>, T> transformer() {

        return new Observable.Transformer() {

            @Override
            public Object call(Object observable) {

                return ((Observable) observable).map(new HandleFuc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
            }
        };
    }

    public Observable.Transformer<JSONObject, JSONObject> transformerData() {

        return new Observable.Transformer() {

            @Override
            public Object call(Object observable) {
                return ((Observable) observable).map(new HandleFuc2()).onErrorResumeNext(new HttpResponseFunc<JSONObject>());
            }
        };
    }

    public <T> Observable<T> switchSchedulers(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override
        public Observable<T> call(Throwable t) {
            Log.e("OkHttp", "HttpResponseFunc ", t);
            t.printStackTrace();
            return Observable.error(ExceptionHandle.handleException(t));
        }
    }

    private class HandleFuc<T> implements Func1<BaseResponse<T>, T> {
        @Override
        public T call(BaseResponse<T> response) {
            if (!response.isOk())
                throw new RuntimeException(response.getCode() + "" + response.getMsg() != null ? response.getMsg() : "");
            Log.e("TEST", "HandleFuc response.getData() " + response.getData() + " getCode " + response.getCode() + " getMsg " + response.getMsg() + " getElapsed " + response.getElapsed());
            return response.getData();
        }
    }

    private class HandleFuc2 implements Func1<JSONObject, JSONObject> {
        @Override
        public JSONObject call(JSONObject jsonobj) {

            if (JSONApi.Analysis(jsonobj)) {//解析逻辑数据
                try {
                    jsonobj = jsonobj.getJSONObject("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    return jsonobj;
                }
            } else {
                try {
                    String respMsg = jsonobj.getString("msg");
                    throw new RuntimeException(jsonobj.getInt("code") + "" + respMsg != null ? respMsg : "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonobj;
        }
    }


    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public static <T> T execute(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return null;
    }


    /**
     * DownSubscriber
     *
     * @param <ResponseBody>
     */
    class DownSubscriber<ResponseBody> extends Subscriber<ResponseBody> {
        CallBack callBack;

        public DownSubscriber(CallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void onStart() {
            super.onStart();
            if (callBack != null) {
                callBack.onStart();
            }
        }

        @Override
        public void onCompleted() {
            if (callBack != null) {
                callBack.onCompleted();
            }
        }

        @Override
        public void onError(Throwable e) {
            if (callBack != null) {
                callBack.onError(e);
            }
        }

        @Override
        public void onNext(ResponseBody responseBody) {
            DownLoadManager.getInstance(callBack).writeResponseBodyToDisk(mContext, (okhttp3.ResponseBody) responseBody);

        }
    }


    public static RequestBody CreateRequestBody(Map<String, Object> body) {
        JSONObject jsbody = new JSONObject(body);
        JSONObject jsheader = new JSONObject();
        try {
            jsheader.put("os", "android");
            if (BaseApplication.getInstance().hasLogin()) {
                jsheader.put("token", BaseApplication.getInstance().getToken());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("header", jsheader);
            jsonRequest.put("body", jsbody);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("a", "jsonRequest:" + jsonRequest);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonRequest.toString());
    }
}
