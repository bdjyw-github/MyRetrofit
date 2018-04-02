package com.idophin.task;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.idophin.util.NetworkUtil;

import org.json.JSONObject;

import rx.Subscriber;

/**
 * BaseSubscriber
 * 处理请求结果
 */
public abstract class BaseSubscriber extends Subscriber<JSONObject> {
    private Context context;
    private boolean isNeedCahe;

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onError(Throwable e) {
        Log.e("Tamic", e.getMessage());
        // todo error somthing
        e.printStackTrace();

        if (e instanceof ExceptionHandle.ResponeThrowable) {
            onError((ExceptionHandle.ResponeThrowable) e);
        } else {
            onError(new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e("Tamic", "onStart");

        // todo some common as show loadding  and check netWork is NetworkAvailable
        // if  NetworkAvailable no !   must to call onCompleted
        if (!NetworkUtil.isNetworkAvailable(context)) {
            Toast.makeText(context, "无网络，读取缓存数据", Toast.LENGTH_SHORT).show();
            onCompleted();
        }

    }

    @Override
    public void onCompleted() {
        Log.e("Tamic", "onCompleted");
        // todo some common as  dismiss loadding
    }


    public abstract void onError(ExceptionHandle.ResponeThrowable e);

    //public abstract void onResponse(T jsonobj);

}
