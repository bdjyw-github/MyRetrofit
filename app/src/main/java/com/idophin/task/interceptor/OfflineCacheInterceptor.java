package com.idophin.task.interceptor;

import android.content.Context;

import com.idophin.util.NetworkUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liyanchuan on 2018/3/7.
 *
 *
 */

public class OfflineCacheInterceptor implements Interceptor {


    private Context context;

    public OfflineCacheInterceptor(Context context) {
        this.context = context;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        /**
         * 未联网获取缓存数据
         */
        if (!NetworkUtil.isNetworkAvailable(context)) {
            //无网情况下，缓存失效后(maxage过期) 20秒 仍然有效
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(20, TimeUnit.SECONDS)
                    .build();

            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build();
        }

        return chain.proceed(request);
    }
}
