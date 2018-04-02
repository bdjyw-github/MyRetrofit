package com.idophin.task.callback;

import org.json.JSONObject;

public interface TastListener {
	public void onResponse(JSONObject jsonobj);
	public void onResponseErro(int erroid);//数据提示错误
	//public void onErrorResponse(VolleyError error);
}
