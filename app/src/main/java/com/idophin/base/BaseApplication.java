package com.idophin.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.idophin.farmingtong.entity.User;
import com.idophin.farmingtong.login.LoginActivity;
import com.idophin.util.Constants;
import com.idophin.util.SharePreferenceUtil;
import com.idophin.util.Util;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

public class BaseApplication extends Application {

    public static final String TAG = BaseApplication.class.getSimpleName();

    private User mUser;
    private SharedPreferences sSharedPreferences;

    private boolean isNetWorkConn;

    private int screenwidth;
    private int screenHeight;
    protected static BaseApplication mInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        sSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        screenwidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!gprs.isConnected() && !wifi.isConnected()) {
            isNetWorkConn = false;
        } else {
            isNetWorkConn = true;
        }
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        initUser();
    }

    private String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }


    public boolean hasLogin() {
        //return false;
        return mUser != null && !TextUtils.isEmpty(mUser.getUserId());
    }

    public String getToken(){
        return null;
    }

    public int getScreenwidth() {
        return screenwidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public boolean clearCachedRuntimeData() {
        Editor editor = sSharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }

    public Serializable getCachedRuntimeData(String tag) {
        String serializeString = sSharedPreferences.getString(tag, null);
        try {
            if (serializeString != null && serializeString.length() > 0) {
                return Util.deserializeObject(serializeString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean putCachedRuntimeData(String tag, Serializable data) {
        try {
            String serializeString = Util.serializeObject(data);
            Editor editor = sSharedPreferences.edit();
            editor.putString(tag, serializeString);
            //return editor.commit();
            editor.apply();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 获取当前应用程序的版本号。
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public boolean isNetWorkConn() {
        return isNetWorkConn;
    }

    public void setNetWorkConn(boolean isNetWorkConn) {
        this.isNetWorkConn = isNetWorkConn;
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersionInfo() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendLocalBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(getInstance()).sendBroadcastSync(intent);
    }

    public static void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        LocalBroadcastManager.getInstance(getInstance()).registerReceiver(receiver, filter);
    }

    public static void unregisterLocalReceiver(BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(getInstance()).unregisterReceiver(receiver);
    }

    /**
     * 获取手机序列号
     */
    public String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    /**
     * 获取手机型号
     */
    public String getPhoneModel() {

        String model = null;

        try {
            model = android.os.Build.MODEL;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return model;
    }


    public User getUser() {
        if (mUser == null) {
            initUser();
        }
        return mUser;
    }

    public void initUser() {
        Log.e("TEST", "initUser");
        if (mUser == null) {
            mUser = new User();
        }
        String mUserString = (String) SharePreferenceUtil.getCachedRuntimeData(sSharedPreferences, Constants.PREF_LOGININFO);
        if (mUserString != null) {
            mUser = mUser.toUser(mUserString);
        }
    }

    public User getUser(Activity activity) {
        if (mUser == null) {
            initUser();
        }
        if (!hasLogin()) {
            Intent i = new Intent(activity, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return null;
        }
        return mUser;
    }

    public void saveUser(User user) {
        mUser = user;
        putCachedRuntimeData(Constants.PREF_LOGININFO, mUser == null ? (new JSONObject()).toString() : mUser.toSaveString());
    }

    public void setUser(User mUser) {
        if (mUser != null) {
            this.mUser = null;
        }
        this.mUser = mUser;
    }

}
