package com.idophin.farmingtong.entity;

import android.content.Context;
import android.util.Log;

import com.idophin.base.BaseApplication;
import com.idophin.entity.UploadFile;
import com.idophin.task.BaseSubscriber;
import com.idophin.task.ExceptionHandle;
import com.idophin.task.RetrofitClient;
import com.idophin.task.callback.CallBackListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liyanchuan on 2018/3/8.
 */

public class UserImpl {

    private Context mContext;


    public UserImpl(Context context) {
        mContext = context;
    }

    /**
     * 登录
     *
     * @param phone
     * @param mima
     * @param callBackListener
     */
    public void login(String phone, String mima, final CallBackListener callBackListener) {
        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("phone", phone);
        body.put("mima", mima);
        body.put("type", "login");

        RetrofitClient.getInstance(mContext).createBaseApi().post("Public"
                , body, new BaseSubscriber(mContext) {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        if (callBackListener != null) {
                            callBackListener.onResponseErro(e.code);
                        }
                    }

                    @Override
                    public void onNext(JSONObject data) {
                        Log.e("TEST", "onResponse data " + data.toString());
                        if (callBackListener != null) {
                            callBackListener.onResponseOK();
                        }
                    }
                });
    }

    /**
     * 修改密码
     *
     * @param phone
     * @param callBackListener
     */
    public void getValidationCode(String phone, final CallBackListener callBackListener) {
        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("password", "123456");
        body.put("mima", "123456");
        body.put("id", "1");

        RetrofitClient.getInstance(mContext).createBaseApi().put("ModifyPassword"
                , body, new BaseSubscriber(mContext) {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        if (callBackListener != null) {
                            callBackListener.onResponseErro(e.code);
                        }
                    }

                    @Override
                    public void onNext(JSONObject data) {
                        Log.e("TEST", "onResponse data " + data.toString());
                        if (callBackListener != null) {
                            callBackListener.onResponseOK();
                        }
                    }
                });
    }

    /**
     * 获取验证码
     *
     * @param id
     * @param callBackListener
     */
    public void MisAutoSnt(String id, final CallBackListener callBackListener) {
        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("id", id);

        RetrofitClient.getInstance(mContext).createBaseApi().delete("MisAutoSnt"
                , body, new BaseSubscriber(mContext) {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        if (callBackListener != null) {
                            callBackListener.onResponseErro(e.code);
                        }
                    }

                    @Override
                    public void onNext(JSONObject data) {
                        Log.e("TEST", "onResponse data " + data.toString());
                        if (callBackListener != null) {
                            callBackListener.onResponseOK();
                        }
                    }
                });
    }

    /**
     * 发帖
     *
     * @param narong
     * @param callBackListener
     */
    public void tiezhi(String narong, ArrayList<UploadFile> files, final CallBackListener callBackListener) {
        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("narong", narong);
        body.put("dizhi", "dfd");
        body.put("shifouyunxupinlun", "1");
        body.put("yonghu", "" + BaseApplication.getInstance().getUser().getUserId());

        RetrofitClient.getInstance(mContext).createBaseApi().postWithFile("MisAutoSnt"
                , body, files,"tupian",new BaseSubscriber(mContext) {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        if (callBackListener != null) {
                            callBackListener.onResponseErro(e.code);
                        }
                    }

                    @Override
                    public void onNext(JSONObject data) {
                        Log.e("TEST", "onResponse data " + data.toString());
                        if (callBackListener != null) {
                            callBackListener.onResponseOK();
                        }
                    }
                });
    }


}
