package com.idophin.farmingtong.entity;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by liyanchuan on 2018/3/2.
 */

public class User implements Serializable{

    public String getUserId() {
        //return userId;
        return "2";
    }

    private String userId;


    public User toUser(String str) {
        Log.e("TEST", "toUser JS " + str);
        User user = new User();
        if (str == null || str.equals("")) {
            return user;
        }
        String phone = null;
        String token = null;
        String id = null;
        try {
            JSONObject js = new JSONObject(str);
            if (js.has("phone")) {
                phone = js.getString("phone");
            }
            if (js.has("token")) {
                token = js.getString("token");
            }
            if (js.has("id")) {
                id = js.getString("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("TEST", "JS phone:" + phone + id);
        if (phone != null && phone.length() > 0 && token != null && token.length() > 0&& id != null && id.length() > 0) {
//            user.setPhone(phone);
//            user.setToken(token);
//            user.setUserId(Integer.parseInt(id));
        }
        return user;
    }

    public String toSaveString() {
        String str = "";
        JSONObject js = new JSONObject();
        try {
//            js.put("token", getToken());
//            js.put("phone", getPhone());
            js.put("id", getUserId());
            str = js.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("TEST","toSaveString "+str);
        return str;
    }
}
