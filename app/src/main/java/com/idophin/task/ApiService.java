package com.idophin.task;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by liyanchuan on 2018/3/5.
 */

public interface  ApiService {
    public static final String Base_URL = "http://ip.taobao.com/";
    /**
     *普通写法
     */
    @GET("service/getIpInfo.php/")
    Observable<ResponseBody> getData(@Query("ip") String ip);


    @GET("{url}")
    Observable<ResponseBody> executeGet(
            @Path("url") String url,
            @QueryMap Map<String, String> maps);
    @GET("{url}")
    Observable<ResponseBody> executeGet(
            @Path("url") String url,
            @QueryMap Map<String, String> headers,
            @QueryMap Map<String, String> maps
    );


    @POST("{url}")
    Observable<ResponseBody> executePost(
            @Path("url") String url,
            @FieldMap Map<String, String> maps);
    @POST("{url}")
    Observable<ResponseBody> executePost(
            @Path("url") String url,
            @FieldMap Map<String, String> headers,
            @FieldMap Map<String, String> maps
    );

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> upLoadFile(
            @Path("url") String url,
            @Part("image\"; filename=\"image.jpg") RequestBody avatar
            );

    @POST("{url}")
    Call<ResponseBody> uploadFiles(
            @Url String url,
            @Part("filename") String description,
            @PartMap()  Map<String, RequestBody> maps);
}
