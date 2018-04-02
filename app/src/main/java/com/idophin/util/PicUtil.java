package com.idophin.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by liyanchuan on 2018/3/14.
 */

public class PicUtil {
    public  static void selectPic(Activity activity){
         PhotoPicker.builder()
                .setPhotoCount(9)
                .setShowCamera(false)
                .setShowGif(false)
                .setPreviewEnabled(true).start(activity);
    }


    public static boolean saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static String compressAndSaveImage(String srcPath, String saveFileName) {
        return compressAndSaveImage(srcPath, saveFileName, 800f, 480f);
    }

    public static String compressAndSaveImage(String srcPath, String saveFileName, float dfW, float dfH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float hh = dfW;
        float ww = dfH;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }

        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        if (bitmap.getByteCount() / 1024 > 1024) {
            bitmap = compress(bitmap);
        }

        BitmapCache.putBitmapToSD(saveFileName, bitmap);

        String filePath = BitmapCache.FOLDER_NAME + "/" + saveFileName;

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();

        }

        return filePath;
    }

    public static Bitmap compressImage(String srcPath, float dfW, float dfH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float hh = dfW;
        float ww = dfH;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }

        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        // if (bitmap.getByteCount() / 1024 > 2 * 1024) {
        // bitmap = compress(bitmap);
        // }

        return bitmap;
    }

    public static Bitmap compress(Bitmap image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if (image.getByteCount() / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);

        return bitmap;
    }

    /***
     * 图片的缩放方法
     *
     * @param String
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    // public static Bitmap zoomImage(String srcPath, double newWidth, double
    // newHeight) {
    // BitmapFactory.Options newOpts = new BitmapFactory.Options();
    // // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
    // newOpts.inJustDecodeBounds = true;
    // Bitmap bgimage = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
    //
    // newOpts.inJustDecodeBounds = false;
    // int width = newOpts.outWidth;
    // int height = newOpts.outHeight;
    // newOpts.inSampleSize = 1;// 设置缩放比例
    // // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
    // bgimage = BitmapFactory.decodeFile(srcPath, newOpts);
    // if (newOpts.outWidth <= newWidth && newOpts.outHeight <= newHeight) {
    // return bgimage;
    // }
    // // 获取这个图片的宽和高
    //
    // // 创建操作图片用的matrix对象
    // Matrix matrix = new Matrix();
    // // 计算宽高缩放率
    // float scaleWidth = ((float) newWidth) / width;
    // float scaleHeight = ((float) newHeight) / height;
    // // 缩放图片动作
    // matrix.postScale(scaleWidth, scaleHeight);
    // Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) newWidth, (int)
    // newHeight, matrix, true);
    // bgimage.recycle();
    // return bitmap;
    // }

    public static Bitmap zoomImage(String srcPath, double newWidth, double newHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bgimage = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;

        if (newOpts.outWidth <= newWidth && newOpts.outHeight <= newHeight) {
            newOpts.inSampleSize = 1;// 设置缩放比例
            // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            bgimage = BitmapFactory.decodeFile(srcPath, newOpts);
            return bgimage;
        }

        newOpts.inSampleSize = 2;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bgimage = BitmapFactory.decodeFile(srcPath, newOpts);
        // 创建操作图片用的matrix对象
        width = bgimage.getWidth();
        height = bgimage.getHeight();
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        Log.e("", "width:" + width + " newOpts.outWidth:" + newOpts.outWidth);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        bgimage.recycle();
        return bitmap;
    }
}
