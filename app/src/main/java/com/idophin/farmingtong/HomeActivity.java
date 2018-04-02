package com.idophin.farmingtong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.idophin.base.UploadWrapper;
import com.idophin.entity.UploadFile;
import com.idophin.farmingtong.entity.NewsImpl;
import com.idophin.farmingtong.entity.UserImpl;
import com.idophin.task.CallBack;
import com.idophin.task.RetrofitClient;
import com.idophin.task.callback.CallBackListener;
import com.idophin.task.callback.TastListener;
import com.idophin.util.DataFactory;
import com.idophin.util.PicUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;


public class HomeActivity extends AppCompatActivity {

    private Context mContext;
    private View btn, btn_get, btn_post, btn_json, btn_put,bt_tie, btn_delete, btn_download, btn_upload, btn_myApi, btn_changeHostApi;

    String url1 = "http://img0.imgtn.bdimg.com/it/u=205441424,1768829584&fm=21&gp=0.jpg";
    String url2 = "http://wap.dl.pinyin.sogou.com/wapdl/hole/201607/05/SogouInput_android_v8.3_sweb.apk?frm=new_pcjs_index";
    String url3 = "http://apk.hiapk.com/web/api.do?qt=8051&id=723";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = HomeActivity.this;

        btn_put = findViewById(R.id.bt_put);
        btn_delete = findViewById(R.id.bt_delete);
        bt_tie = findViewById(R.id.bt_tie);

        btn_get = findViewById(R.id.bt_get);
        btn_post = findViewById(R.id.bt_post);
        btn_download = findViewById(R.id.bt_download);
        btn_upload = findViewById(R.id.bt_upload);
        btn_myApi = findViewById(R.id.bt_my_api);
        btn_changeHostApi = findViewById(R.id.bt_changeHostApi);


        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final NewsImpl impl = new NewsImpl(mContext);
                impl.queryNewslist(new CallBackListener() {
                    @Override
                    public void onResponseOK() {
                        impl.getmNews();
                    }

                    @Override
                    public void onResponseErro(int erroid) {

                    }
                });

            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserImpl impl = new UserImpl(mContext);
                impl.login("15223418197", "123456", new CallBackListener() {
                    @Override
                    public void onResponseOK() {

                    }

                    @Override
                    public void onResponseErro(int erroid) {

                    }
                });

            }
        });
        btn_put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserImpl impl = new UserImpl(mContext);
                impl.getValidationCode("15223418197", new CallBackListener() {
                    @Override
                    public void onResponseOK() {

                    }

                    @Override
                    public void onResponseErro(int erroid) {

                    }
                });

            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserImpl impl = new UserImpl(mContext);
                impl.MisAutoSnt("123456", new CallBackListener() {
                    @Override
                    public void onResponseOK() {

                    }

                    @Override
                    public void onResponseErro(int erroid) {

                    }
                });

            }
        });


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicUtil.selectPic(HomeActivity.this);

            }
        });
        bt_tie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserImpl impl = new UserImpl(mContext);
                impl.tiezhi("123456",mFiles, new CallBackListener() {
                    @Override
                    public void onResponseOK() {
                        Log.e("TEST","发帖 onResponseOK");
                    }

                    @Override
                    public void onResponseErro(int erroid) {

                    }
                });

            }
        });

        btn_download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RetrofitClient.getInstance(mContext).createBaseApi().download(url3, new CallBack() {

                            @Override
                            public void onStart() {
                                super.onStart();
                                Toast.makeText(mContext, url1 + "  is  start", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onProgress(long fileSizeDownloaded) {
                                super.onProgress(fileSizeDownloaded);
                                Toast.makeText(mContext, " downLoadeing, download:" + fileSizeDownloaded, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSucess(String path, String name, long fileSize) {
                                Toast.makeText(mContext, name + " is  downLoaded", Toast.LENGTH_SHORT).show();

                            }
                        }
                );
            }
        });


    }

    private ArrayList<String> mFilePathes;
    private ArrayList<UploadFile> mFiles;
    private void uploadFile(ArrayList<String> filePathes){
        UploadWrapper.uploadFile(filePathes, new TastListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject.has("data")){
                    try {

                        JSONObject data =  jsonObject.getJSONObject("data");
                        JSONArray array = data.getJSONArray("list");
                        mFiles = DataFactory.jsonToArrayList(array.toString(), UploadFile.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onResponseErro(int erroid) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                mFilePathes = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                //显示图片icon
                if (mFilePathes != null && mFilePathes.size() > 0) {
                    for (int i = 0; i < mFilePathes.size(); i++) {
                        String picturePath = mFilePathes.get(i);
                        String fileSuffix = picturePath.substring(picturePath.lastIndexOf("."));
                        String pictureName = picturePath + "/" + System.currentTimeMillis() + fileSuffix;
                        String path = PicUtil.compressAndSaveImage(picturePath, pictureName);
                        //mFilePathes.set(i, path);
//                    BitmapEntity tBitmapEntity = new BitmapEntity("", mFilePathes.get(i), 0, "", 0);
//                    adapter.add(tBitmapEntity);
                    }

                    uploadFile(mFilePathes);

                }
            }
        }

    }
}