package org.dream.dreambox.common.component.pan.kuaipan.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class KuaiPanUtil {

    /**
     * 随机生成指定长度的字符串。为提供nonce。
     * @param length
     * @return
     */
    public static String getRandomNonce(int length){
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for(int i = 0; i < length; i++){
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }
    
    /**
     * 为URL进行转码。
     * @param url
     * @return
     */
    public static String encodeUrl(String url){
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * 生成签名数据
     * @param data 待加密的数据
     * @param key  加密使用的key
     * @return 
     */
    public static byte[] encodeHmacSHA(byte[] data, byte[] key) {
        try {
            String method = "HmacSHA1";
            SecretKeySpec k = new SecretKeySpec(key, method);
            Mac mac = Mac.getInstance(method);
            mac.init(k);
            return mac.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
