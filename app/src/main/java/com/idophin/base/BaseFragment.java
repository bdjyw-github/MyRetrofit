package com.idophin.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {

	private View mContentView;
	protected Context mContext;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(mContext == null){
			mContext = BaseApplication.getInstance();
		}
		init();
	}

	public void showCoshowFragment(boolean requestData){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = inflater.inflate(setLayoutResourceID(), container, false);//setContentView(inflater, container);
		setUpView();
		return mContentView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setUpData();
	}

	protected abstract int setLayoutResourceID();

	protected void init() {

	}

	protected abstract void setUpView();

	protected abstract void setUpData();

	protected <T extends View> T $(int id) {
		return (T) mContentView.findViewById(id);
	}

	// protected abstract View setContentView(LayoutInflater inflater, ViewGroup container);

	protected View getContentView() {
		return mContentView;
	}

	public Context getMContext() {
		return mContext;
	}

	public void onRefresh(){}


	private int index;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
