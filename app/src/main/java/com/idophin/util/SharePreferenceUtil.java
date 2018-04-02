package com.idophin.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.IOException;
import java.io.Serializable;

public class SharePreferenceUtil {

	public static Serializable getCachedRuntimeData(
            SharedPreferences sSharedPreferences, String tag) {
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

	public static boolean putCachedRuntimeData(
            SharedPreferences sSharedPreferences, String tag, Serializable data) {
		try {
			String serializeString = Util.serializeObject(data);
			Editor editor = sSharedPreferences.edit();
			editor.putString(tag, serializeString);
			return editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
