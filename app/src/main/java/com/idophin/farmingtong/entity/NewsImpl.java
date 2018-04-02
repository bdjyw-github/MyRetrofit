package com.idophin.farmingtong.entity;

import android.content.Context;
import android.util.Log;

import com.idophin.task.BaseSubscriber;
import com.idophin.task.ExceptionHandle;
import com.idophin.task.RetrofitClient;
import com.idophin.task.callback.CallBackListener;
import com.idophin.util.DataFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyanchuan on 2018/3/8.
 */

public class NewsImpl {

    private Context mContext;

    private ArrayList<News> mNews;

    public NewsImpl(Context context){
        mContext = context;
    }

    public void queryNewslist(final CallBackListener callBackListener) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("leixing", "1"); //快讯
        body.put("order", "createtime");
        body.put("pagesize", "5");

        RetrofitClient.getInstance(mContext).createBaseApi().get("MisAutoVyc"
                , body, new BaseSubscriber(mContext) {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        if(callBackListener != null){
                            callBackListener.onResponseErro(e.code);
                        }
                    }

                    @Override
                    public void onNext(JSONObject data) {
                        Log.e("TEST","onResponse data "+data);
                        try {
                            JSONArray array = data.getJSONArray("list");
                            mNews = DataFactory.jsonToArrayList(array.toString(), News.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(callBackListener != null){
                            callBackListener.onResponseOK();
                        }
                    }

                });
    }

    public ArrayList<News> getmNews(){
        return mNews;
    }

}
