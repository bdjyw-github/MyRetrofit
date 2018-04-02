package com.idophin.util;

import android.app.Activity;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsContent extends ContentObserver {

	private Activity activity = null;
	private EditText verifyText = null;
	private String phoneNum = "";

	public SmsContent(Activity activity, Handler handler, EditText verifyText,
                      String phoneNum) {
		super(handler);
		this.activity = activity;
		this.verifyText = verifyText;
		this.phoneNum = phoneNum;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		
		if (TextUtils.isEmpty(phoneNum)) {
			return;
		}
		
		Cursor cursor = null;// 光标
		// 读取收件箱中指定号码的短�?
		cursor = activity.managedQuery(Uri.parse("content://sms/inbox"),
				new String[] { "_id", "address", "read", "body" },
				"address=? and read=?", new String[] { phoneNum, "0" },
				"_id desc");

		if (cursor != null && cursor.getCount() > 0) {

			ContentValues values = new ContentValues();

			values.put("read", "1"); // 修改短信为已读模�?

			cursor.moveToNext();

			int smsbodyColumn = cursor.getColumnIndex("body");

			String smsBody = cursor.getString(smsbodyColumn);

			verifyText.setText(getDynamicPassword(smsBody));

		}

		if (Build.VERSION.SDK_INT < 14) {
			cursor.close();
		}

	}

	/**
	 * 从字符串中截取连�?位数字组�?([0-9]{" + 6 + "})截取六位数字 进行前后断言不能出现数字 用于从短信中获取动�?密码
	 * @param str 短信内容
	 * @return 截取得到�?位动态密�?
	 */
	public static String getDynamicPassword(String str) {
		// 6是验证码的位数一般为六位
		Pattern continuousNumberPattern = Pattern.compile("(?<![0-9])([0-9]{"
				+ 6 + "})(?![0-9])");
		Matcher m = continuousNumberPattern.matcher(str);
		String dynamicPassword = "";
		while (m.find()) {
			dynamicPassword = m.group();
		}

		return dynamicPassword;
	}

}