package com.idophin.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static final String DAY_NIGHT_MODE = "daynightmode";
    public static final String DEVIATION = "deviationrecalculation";
    public static final String JAM = "jamrecalculation";
    public static final String TRAFFIC = "trafficbroadcast";
    public static final String CAMERA = "camerabroadcast";
    public static final String SCREEN = "screenon";
    public static final String THEME = "theme";
    public static final String ISEMULATOR = "isemulator";

    public static final boolean DAY_MODE = false;
    public static final boolean NIGHT_MODE = true;
    public static final boolean YES_MODE = true;
    public static final boolean NO_MODE = false;
    public static final boolean OPEN_MODE = true;
    public static final boolean CLOSE_MODE = false;

    /*
     * TypedValue.COMPLEX_UNIT_DIP
     */
    public static float getDimension(Context context, int unit, float value) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(unit, value, dm);
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    public static void AnimationRepeat(final View v, Animation animation) {
        v.setAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                v.startAnimation(animation);
            }
        });
        v.startAnimation(animation);
    }

    public static String serializeObject(Serializable object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);

        byte[] serializeData = baos.toByteArray();
        oos.close();
        baos.close();
        return Base64.encode(serializeData);
    }

    public static Serializable deserializeObject(String serializeString) throws OptionalDataException, ClassNotFoundException, IOException {
        byte[] serializeData = Base64.decode(serializeString);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializeData);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object object = ois.readObject();

        ois.close();
        bais.close();

        return (Serializable) object;
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

    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern.compile("^(1)\\d{10}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    public static boolean isEmail(String mail) {
        mail = mail.toLowerCase();
        if (mail.endsWith(".con"))
            return false;

        if (mail.endsWith(".cm"))
            return false;

        if (mail.endsWith("@gmial.com"))
            return false;

        if (mail.endsWith("@gamil.com"))
            return false;

        if (mail.endsWith("@gmai.com"))
            return false;

        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher = pattern.matcher(mail);

        return matcher.matches();
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isFloat(String str) {
        Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isBank(String str) {
        Pattern pattern = Pattern.compile("^\\d{14,19}$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 获取屏幕分辨�?
     *
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽�?
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高�?
        int result[] = {width, height};
        return result;
    }

    public static boolean isIdenNum(String str) {

        Pattern pattern = Pattern.compile("([0-9]{17}([0-9]|[X|x]))|([0-9]{15})");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }

        return true;
    }

    public static void showToast(Context context, int StringResId) {
        Toast.makeText(context, StringResId, Toast.LENGTH_SHORT).show();
    }

    public static String getVersionName(Context context) throws Exception {
        // 获取packagemanager的实�?
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名�?代表是获取版本信�?
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    public static boolean isNumCharacter_(String str) {
        Matcher matcher = Pattern.compile("^[0-9a-zA-Z_-]+$").matcher(str);

        return matcher.find();
    }

    public static String getMoneyStrWithDot(double money, String format) {
        DecimalFormat df = new DecimalFormat(format);
        String str = df.format(money);

        return str;
    }


    public static Bitmap compressImage(String srcPath, float dfW, float dfH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // �?��读入图片，此时把options.inJustDecodeBounds 设回true�?
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        Log.e("TEST", "compressImage w=" + w + "; h=" + h + "; dfW=" + dfW + "; dfH=" + dfH);

        float hh = dfW;
        float ww = dfH;
        // 缩放比�?由于是固定比例缩放，只用高或者宽其中�?��数据进行计算即可
        int be = 1;// be=1表示不缩�?

//        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩�?
//            be = (int) (newOpts.outWidth / ww);
//        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩�?
//            be = (int) (newOpts.outHeight / hh);
//        }

        if (h > dfH || w > dfW) {
            //使用需要的宽高的最大值来计算比率
            final float suitedValue = dfH > dfW ? dfH : dfW;
            final float heightRatio = Math.round((float) h / (float) suitedValue);
            final float widthRatio = Math.round((float) w / (float) suitedValue);

            be = (int) (heightRatio > widthRatio ? heightRatio : widthRatio);//用最大
        }

        if (be <= 0)
            be = 1;

        Log.e("TEST", "compressImage be=" + be);
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false�?
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
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);// 这里压缩50%，把压缩后的数据存放到baos�?
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);

        return bitmap;
    }


    public static Bitmap zoomImage(String srcPath, double newWidth, double newHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // �?��读入图片，此时把options.inJustDecodeBounds 设回true�?
        newOpts.inJustDecodeBounds = true;
        Bitmap bgimage = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;

        if (newOpts.outWidth <= newWidth && newOpts.outHeight <= newHeight) {
            newOpts.inSampleSize = 1;// 设置缩放比例
            // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false�?
            bgimage = BitmapFactory.decodeFile(srcPath, newOpts);
            return bgimage;
        }

        newOpts.inSampleSize = 2;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false�?
        bgimage = BitmapFactory.decodeFile(srcPath, newOpts);
        // 创建操作图片用的matrix对象
        width = bgimage.getWidth();
        height = bgimage.getHeight();
        Matrix matrix = new Matrix();
        // 计算宽高缩放�?
        Log.e("", "width:" + width + " newOpts.outWidth:" + newOpts.outWidth);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        bgimage.recycle();
        return bitmap;
    }

    public static String formatPercent(double value) {
        DecimalFormat myformat5 = null;
        myformat5 = new DecimalFormat();
        myformat5.applyPattern("#.####%");

        return myformat5.format(value);

    }

    public static String formatDouble(double value) {
        DecimalFormat myformat5 = null;
        myformat5 = new DecimalFormat();
        myformat5.applyPattern("#.####");

        return myformat5.format(value);

    }

    /**
     * 密码规则/^\S{6,32}$/
     *
     * @param str
     * @return
     */
    public static boolean isPassword(String str) {
        Matcher matcher = Pattern.compile("^\\S{6,16}$").matcher(str);
        return matcher.find();
    }

    public static boolean isCN(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            if (bytes.length == str.length()) {
                return false;
            } else {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static String formatTwoDotDouble(double value) {
        DecimalFormat myformat5 = null;
        myformat5 = new DecimalFormat();
        myformat5.applyPattern("0.00");

        return myformat5.format(value);

    }

    public static final int APP_RUN_STATUS_DEAD = -1;
    public static final int APP_RUN_STATUS_FOREGROUND = 0;
    public static final int APP_RUN_STATUS_BACKGROUND = 1;

    public static int getAppRunStatus(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return APP_RUN_STATUS_BACKGROUND;
                } else {
                    Log.e(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return APP_RUN_STATUS_FOREGROUND;
                }
            }
        }
        return APP_RUN_STATUS_DEAD;
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName ：应用包名
     * @return
     */
    public static boolean isInstallAPP(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    public static boolean isInstallThirdMapAPP(Context context) {
        boolean isStallBaidu = Util.isInstallAPP(context, "com.baidu.BaiduMap");
        boolean isStallGaode = Util.isInstallAPP(context, "com.autonavi.minimap");
        if (isStallBaidu || isStallBaidu) {
            return true;
        } else {
            return false;
        }
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * Java将Unix时间戳转换成指定格式日期字符串
     *
     * @param timestampString 时间戳 如："1473048265";
     * @param formats         要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String TimeStamp2Date(String timestampString, String formats) {
        if (TextUtils.isEmpty(timestampString) || "null".equals(timestampString)) {
            return "";
        }
        if (TextUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat(formats, Locale.CHINA);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String date = sdf.format(new Date(timestamp));
        return date;
    }

    public static boolean checkExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 解析讯飞结果
     *
     * @param json
     * @return
     */
    public static String parseReconizerResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    // 腾讯
    private static Toast mToast;
    private static Dialog mProgressDialog;

    public static final void showResultDialog(Context context, String msg,
                                              String title) {
        if (msg == null) return;
        String rmsg = msg.replace(",", "\n");
        Log.d("Util", rmsg);
        new AlertDialog.Builder(context).setTitle(title).setMessage(rmsg)
                .setNegativeButton("知道了", null).create().show();
    }

    /**
     * 打印消息并且用Toast显示消息
     *
     * @param activity
     * @param message
     * @param logLevel 填d, w, e分别代表debug, warn, error; 默认是debug
     */
    public static final void toastMessage(final Activity activity,
                                          final String message, String logLevel) {
        if ("w".equals(logLevel)) {
            Log.w("sdkDemo", message);
        } else if ("e".equals(logLevel)) {
            Log.e("sdkDemo", message);
        } else {
            Log.d("sdkDemo", message);
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mToast != null) {
                    mToast.cancel();
                    mToast = null;
                }
                mToast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    /**
     * 打印消息并且用Toast显示消息
     *
     * @param activity
     * @param message
     */
    public static final void toastMessage(final Activity activity,
                                          final String message) {
        toastMessage(activity, message, null);
    }

    public static void release() {
        mProgressDialog = null;
        mToast = null;
    }

    public static final void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                boolean res = item.delete();
                Log.e("TEST", "delete " + item.getPath() + "; res = " + res);
            }
        }
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager

//        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
//        if (pinfo != null) {
//            for (int i = 0; i < pinfo.size(); i++) {
//                String pn = pinfo.get(i).packageName;
//                Log.e("TEST", "isWeixinAvilible: packageName="+pn);
//                if (pn.equals("com.tencent.mm")) {
//                    return true;
//                }
//            }
//        }
        Intent intent = packageManager.getLaunchIntentForPackage("com.tencent.mm");
        if (intent != null) {
            return true;
        }

        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
//        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
//        if (pinfo != null) {
//            for (int i = 0; i < pinfo.size(); i++) {
//                String pn = pinfo.get(i).packageName;
//                if (pn.equals("com.tencent.mobileqq")) {
//                    return true;
//                }
//            }
//        }

        Intent intent = packageManager.getLaunchIntentForPackage("com.tencent.mobileqq");
        if (intent != null) {
            return true;
        }
        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWeiboClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage("com.sina.weibo");
        if (intent != null) {
            return true;
        }
        return false;
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        if (TextUtils.isEmpty(input)){
            return input;
        }
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        if (TextUtils.isEmpty(str)){
            return str;
        }

        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
