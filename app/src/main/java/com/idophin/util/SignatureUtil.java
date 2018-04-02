/*
 * Copyright (c) 2017. 4. 27. by Idophin Co.Ltd. All rights reserved.
 *
 */

package com.idophin.util;


import android.util.Base64;

import com.idophin.entity.PariKeyValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


@SuppressWarnings("restriction")
public class SignatureUtil {


    private static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * 生成签名数据
     *
     * @param data 待加密的数据
     * @param key  加密使用的key
     */
    public static String getSignature(String data, String key) {
        try {
            byte[] keyBytes = key.getBytes("UTF-8");
            byte[] dataBytes = data.getBytes("UTF-8");

            return generateSignature(dataBytes, keyBytes);
        } catch (Exception e) {
            return null;
        }
    }

    // 对 data 以key 进行hash算法【MAC-SHA1】
    private static String generateSignature(byte[] data, byte[] key)
            throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data);
        return getBase64Signature(rawHmac);
    }

    // 将原始二进制数据数据，进行base64编码
    private static String getBase64Signature(byte[] srcSignature) {
        String base64Text = Base64.encodeToString(srcSignature, Base64.NO_WRAP);
        return base64Text;
    }


    /**
     * 方法说明：将list中的PariKeyValue元素，按照key属性的升序排列
     * 方法参数：
     *
     * @param entryList 返回类型：void
     */
    public static void dictionaryAscendSort(List<PariKeyValue> entryList) {

        // 将得到的所有参数，除开签名(signature),其他的按字典升序排序
        Collections.sort(entryList, new Comparator<PariKeyValue>() {
            @Override
            public int compare(PariKeyValue thisPair, PariKeyValue thatPair) {
                int keyCompare = thisPair.key.compareTo(thatPair.key);
                return keyCompare;
            }
        });

    }


    /**
     * 方法说明：将请求参数对象以key1=value1&key2=value2&key3=value3&……拼接成参数字符串
     * 方法参数：
     *
     * @param entryList
     * @return 返回类型：String
     */
    public static String joinParamKeyValueStr(List<PariKeyValue> entryList) {
        StringBuffer paramdata = new StringBuffer();
        for (int i = 0; i < entryList.size(); i++) {
            paramdata.append(entryList.get(i).key + "=" + entryList.get(i).value + "&");
        }
        return paramdata.toString();
    }

    public static String joinParamKeyValueEndStr(List<PariKeyValue> entryList) {
        StringBuffer paramdata = new StringBuffer();
        for (int i = 0; i < entryList.size(); i++) {
            if (i != entryList.size() - 1)
                paramdata.append(entryList.get(i).key + "=" + entryList.get(i).value + "&");
            else
                paramdata.append(entryList.get(i).key + "=" + entryList.get(i).value);
        }
        return paramdata.toString();
    }

    public static String SingMessage(ArrayList<PariKeyValue> list, String random, String token) {
        String joinParamKeyValueStr = "";
        if (list != null && list.size() != 0) {
            SignatureUtil.dictionaryAscendSort(list);
            joinParamKeyValueStr = joinParamKeyValueEndStr(list);
        }
        joinParamKeyValueStr += random;
        String sin = getSignature(joinParamKeyValueStr, token);
        return sin;
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


}
