package com.idophin.task.interceptor;

import android.util.Log;

import com.idophin.base.BaseApplication;
import com.idophin.entity.PariKeyValue;
import com.idophin.util.Constants;
import com.idophin.util.RSAUtils;
import com.idophin.util.SHA1Utils;
import com.idophin.util.SignatureUtil;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * BaseInterceptor，use set okhttp call header
 * Created by Tamic on 2016-06-30.
 */
public class BaseInterceptor implements Interceptor {

    private Map<String, String> headers;

    public BaseInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey)).build();
            }
        }

        String mRanStr = SignatureUtil.getRandomString(8);
        String mToken = BaseApplication.getInstance().getToken() != null ? BaseApplication.getInstance().getToken() : "12121212";
        ArrayList<PariKeyValue> entryList = new ArrayList<>();
        Request request = chain.request();
        if (request.method().equals("GET")) {
            HttpUrl httpUrl = chain.request().url();
            Set<String> queryParameterNames = httpUrl.queryParameterNames();
            for (String name : queryParameterNames) {
                String value = httpUrl.queryParameter(name);
                Log.e("OkHttp", "name = " + name + " value = " + value);
                entryList.add(new PariKeyValue(name, value));
            }
        } else {
            if (request.body() instanceof FormBody) {
                // FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();
                //把原来的参数添加到新的构造器，（因为没找到直接添加，所以就new新的）

                for (int i = 0; i < formBody.size(); i++) {
                    Log.e("OkHttp", "name = " + formBody.encodedName(i) + " value = " + formBody.value(i));
                    //bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                    if ("swf_file".equals(formBody.encodedName(i))) {
                        entryList.add(new PariKeyValue(formBody.encodedName(i), formBody.value(i)));
                    }else {
                        entryList.add(new PariKeyValue(formBody.encodedName(i), formBody.encodedValue(i)));
                    }
                }
            } else {
                Log.e("OkHttp", "not FormBody ");
            }
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

        builder.addHeader("crypt", crypt);
        builder.addHeader("token", RasToken);
        builder.addHeader("key", RasKey);
        builder.addHeader("version", Constants.URL_V1);
        builder.addHeader("author", Constants.Service_Key);


        Response r = chain.proceed(builder.build());
        return r;

    }
}