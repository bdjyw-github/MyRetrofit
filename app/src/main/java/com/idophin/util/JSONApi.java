package com.idophin.util;

import android.content.Intent;
import android.widget.Toast;

import com.idophin.base.BaseApplication;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class JSONApi {

	public static boolean Analysis(JSONObject js) {
		return Analysis(js, true);
	}

	public static boolean Analysis(JSONObject js, boolean isShowToast) {
		if (js == null) {
			return false;
		}
		try {
			int respCode =js.getInt("code");
			if (respCode != 200) {
				String respMsg = js.getString("msg");
				if (isShowToast) {
					Toast.makeText(BaseApplication.getInstance(), respMsg, Toast.LENGTH_SHORT).show();
				}
				if (respCode == -1) {
					//BaseApplication.getInstance().tokenFailure();
					Intent i = new Intent();
					i.setAction(Constants.ACTION_RELOGIN);
					BaseApplication.sendLocalBroadcast(i);
				}
				return false;
			} else {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

}
