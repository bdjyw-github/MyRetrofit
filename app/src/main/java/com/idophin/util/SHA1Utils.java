package com.idophin.util;

import com.idophin.entity.PariKeyValue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * author: 陈大人
 * description:
 * date: 2017/3/14 0014
 * time: 16:55
 */

public class SHA1Utils {

    public static String getSign(ArrayList<PariKeyValue> list, String randomStr, String token) {
        String sign = "";
        String keyValue = "";
        try {
            if (list != null && list.size() != 0) {

                sort(list);
                StringBuffer buffer = new StringBuffer();

                for (int i = 0; i < list.size(); i++) {
                    PariKeyValue params = list.get(i);
                    String pKey = params.key;
                    String pValue = params.value;
                    //pKey = URLEncoder.encode(pKey, "UTF-8").replaceAll("\\u002B", "%20");
                    //pValue = URLEncoder.encode(pValue, "UTF-8").replaceAll("\\u002B", "%20");
                    String temp = URLEncoder.encode(pValue, "UTF-8");
                    pValue = temp.replaceAll("\\*", "%2A").replaceAll("\\+", "%20").replaceAll("%7E", "~");
                    // 最后一次不添加&加上混淆码
                    if (i == list.size() - 1)
                        buffer.append(pKey + "=" + pValue);
                    else
                        buffer.append(pKey + "=" + pValue).append("&");
                    keyValue = buffer.toString();
                }
            }

            keyValue += randomStr;
            sign = encode(keyValue, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

    public static String encode(String data, String key) {
        try {
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(
                    key.getBytes("UTF-8"), "HmacSHA1");// 加密密钥
            Mac localMac = Mac.getInstance("HmacSHA1");
            localMac.init(localSecretKeySpec);
            localMac.update(data.getBytes("UTF-8"));// 加密内容，这里使用时间
            // 获取加密结果并转BASE64
            String encode = Base64Utils.encode(localMac.doFinal());
            return encode;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 方法说明：将list中的PariKeyValue元素，按照key属性的升序排列
     * 方法参数：
     *
     * @param entryList 返回类型：void
     */
    public static void sort(List<PariKeyValue> entryList) {

        // 将得到的所有参数，除开签名(signature),其他的按字典升序排序
        Collections.sort(entryList, new Comparator<PariKeyValue>() {
            @Override
            public int compare(PariKeyValue thisPair, PariKeyValue thatPair) {
                int keyCompare = thisPair.key.compareTo(thatPair.key);
                return keyCompare;
            }
        });

    }

}
