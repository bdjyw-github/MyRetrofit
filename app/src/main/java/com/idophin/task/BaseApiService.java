package com.idophin.task;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Ｔａｍｉｃ on 2016-07-08.
 * {@link # https://github.com/NeglectedByBoss/RetrofitClient}
 */
public interface BaseApiService {

    // public static final String Base_URL = "http://115.29.136.168:8290/api/";
    //public static final String Base_URL = "https://api.callhsl.com/api/";
    public static final String Base_URL = "http://dev.byhsl.tmlsoft.com:81/api/";
    //public static final String Base_URL = "http://ip.taobao.com/service/";


    @GET("{url}")
    Observable<JSONObject> executeGet(
            @Path("url") String url,
            @QueryMap Map<String, Object> maps
    );

    //@Headers({"Content-type:application/x-www-form-urlencoded;charset=utf-8"})
    @FormUrlEncoded
    @POST("{url}")
    Observable<JSONObject> executePost(
            @Path("url") String url,
            @FieldMap Map<String, Object> maps);

    @FormUrlEncoded
    @PUT("{url}")
    Observable<JSONObject> executePut(
            @Path("url") String url,
            @FieldMap Map<String, Object> maps);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "{url}", hasBody = true)
    Observable<JSONObject> executeDelete(
            @Path("url") String url,
            @FieldMap Map<String, Object> maps);


    //application/json 方式服务器不支持
    @POST("{url}")
    Observable<BaseResponse<JSONObject>> executePostJson(
            @Path("url") String url,
            @Body RequestBody jsonStr);

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> upLoadFile(
            @Path("url") String url,
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody);

    @POST("{url}")
    Observable<JSONObject> uploadFiles(
            @Path("url") String url,
            @HeaderMap Map<String, Object> headers,
            @Body MultipartBody bodies);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

}
