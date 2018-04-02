package com.idophin.util;

import java.security.MessageDigest;

/*
 * MD5 算法
*/
public class MD5 {

    // 全局数组
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private final static char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public MD5() {
    }

    // 返回形式为数字跟字符�?
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 返回形式只为数字
    private static String byteToNum(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }

    // 转换字节数组�?6进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    private static String byteToString2(byte[] datas) {
        String s = "";
        char[] str = new char[2 * 16];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte b = datas[i];
            str[k++] = hexChars[b >>> 4 & 0xf];//高4位
            str[k++] = hexChars[b & 0xf];//低4位
        }
        s = new String(str);
        return s;
    }

//    public static String GetMD5Code(String strObj) {
//        return GetMD5Code(strObj, false);
//    }
//
//    public static String GetMD5Code(String strObj, boolean is16bit) {
//        String resultString = null;
//        try {
//            resultString = new String(strObj);
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            // md.digest() 该函数返回�?为存放哈希�?结果的byte数组
//            resultString = byteToString2(md.digest(strObj.getBytes("GBK")));
//            if (is16bit) {
//                resultString = resultString.substring(8, 24);
//            }
//        } catch (Exception  ex) {
//            ex.printStackTrace();
//        }
//        return resultString;
//    }

    public static String GetMD5Code(String strObj) {
        return GetMD5Code(strObj, false);
    }

    public static String GetMD5Code(String str, boolean is16bit) {
        try {
            /** 创建MD5加密对象 */
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            /** 进行加密 */
            md5.update(str.getBytes());
            /** 获取加密后的字节数组 */
            byte[] md5Bytes = md5.digest();
            String res = "";
            for (int i = 0; i < md5Bytes.length; i++){
                int temp = md5Bytes[i] & 0xFF;
                if (temp <= 0XF){ // 转化成十六进制不够两位，前面加零
                    res += "0";
                }
                res += Integer.toHexString(temp);
            }
            if (is16bit) {
                res = res.substring(8, 24);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        MD5 getMD5 = new MD5();
    }
}
