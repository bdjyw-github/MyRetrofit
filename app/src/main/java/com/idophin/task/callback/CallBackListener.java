package com.idophin.task.callback;

public interface CallBackListener {
	public void onResponseOK();
	public void onResponseErro(int erroid);//数据提示错误
}
