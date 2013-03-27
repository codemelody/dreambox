package org.dream.dreambox.common.component.pan.kuaipan;

import org.apache.commons.lang.StringUtils;
import org.dream.dreambox.common.component.pan.kuaipan.domain.AuthEntity;
import org.dream.dreambox.common.component.pan.kuaipan.domain.RequestTokenRes;
import org.dream.dreambox.common.component.pan.kuaipan.util.JsonUtil;
import org.dream.dreambox.common.component.pan.kuaipan.util.KuaiPanGlobal;
import org.dream.dreambox.common.component.pan.kuaipan.util.KuaiPanUtil;
import org.dream.dreambox.common.util.ThreadSafeHttpClient;

import sun.misc.BASE64Encoder;

/**
 * 
 * @author </a href="mailto:dingjun@llyuntong.com.cn">dingjun</a>
 * @version $Id: Test.java, v 0.1 2013-2-21 上午9:15:25 Administrator Exp $ 
 * @since 1.0
 */
public class KuaiPanOAuth {

    /**
     * 生成基本请求URL。
     * @param consumerKey 必须，String类型， 第1步的 consumer_key。
     * @param signatureMethod 非必须，String类型，请用 "HMAC-SHA1"。
     * @param timestamp 必须，int类型，时间戳毫秒，正整数，和标准时间不超过5分钟。
     * @param nonce 必须，String类型，[0-9A-Za-z_]随机字符串,长度小于32字节。每次请求请使用不同的nonce。
     * @param version 非必须，String类型，请填 "1.0"。
     * @param callback 非必须，String类型，回调函数。
     * @return
     */
    private static String gererateRequestUrl(AuthEntity oauth){
        if(StringUtils.isEmpty(oauth.getConsumerKey()) || oauth.getTimestamp() == null 
                || oauth.getTimestamp() == 0 || StringUtils.isEmpty(oauth.getNonce())){
            throw new RuntimeException("参数异常[consumerKey=" + oauth.getConsumerKey() 
                + ", timestamp=" + oauth.getTimestamp() + ", nonce=" + oauth.getNonce() + "]");
        }
        StringBuffer url = new StringBuffer(KuaiPanGlobal.REQUEST_TOKEN_URL + "?");
        url.append("oauth_signature=").append(gererateOAuthSignature(oauth));
        url.append("&oauth_consumer_key=").append(oauth.getConsumerKey());
        url.append("&oauth_nonce=").append(oauth.getNonce());
        url.append("&oauth_signature_method=").append(oauth.getSignatureMethod());
        url.append("&oauth_timestamp=").append(oauth.getTimestamp().toString().substring(0, 10)); // 快盘要求时间长度为10。
        url.append("&oauth_version=").append(oauth.getVersion());
        if(StringUtils.isNotEmpty(oauth.getCallback())){
            url.append("&oauth_callback=").append(oauth.getCallback());
        }
        return url.toString();
    }
    
    /**
     * 生成签名。
     * @param consumerKey
     * @param signatureMethod
     * @param timestamp
     * @param nonce
     * @param version
     * @param callback
     * @return
     */
    private static String gererateOAuthSignature(AuthEntity oauth) {
        String baseUrl = gererateBaseUrl(oauth);
        String secret = KuaiPanGlobal.CONSUMER_SECRET + "&";
        String base64 = new BASE64Encoder().encode(
            KuaiPanUtil.encodeHmacSHA(baseUrl.getBytes(), secret.getBytes()));
        return base64;
    }

    /**
     * 生成基本的URL，即需要加密的基础串，不含signature的URL
     * @param consumerKey
     * @param signatureMethod
     * @param timestamp
     * @param nonce
     * @param version
     * @param callback
     * @return
     */
    private static String gererateBaseUrl(AuthEntity oauth){
        if(StringUtils.isEmpty(oauth.getConsumerKey()) || oauth.getTimestamp() == null 
                || oauth.getTimestamp() == 0 || StringUtils.isEmpty(oauth.getNonce())){
            throw new RuntimeException("参数异常[consumerKey=" + oauth.getConsumerKey() 
                + ", timestamp=" + oauth.getTimestamp() + ", nonce=" + oauth.getNonce() + "]");
        }
        StringBuffer url = new StringBuffer("GET&" + KuaiPanUtil.encodeUrl(KuaiPanGlobal.REQUEST_TOKEN_URL) + "&");
        StringBuffer params = new StringBuffer();
        params.append("oauth_consumer_key=").append(oauth.getConsumerKey());
        params.append("&oauth_nonce=").append(oauth.getNonce());
        params.append("&oauth_signature_method=").append(oauth.getSignatureMethod());
        params.append("&oauth_timestamp=").append(oauth.getTimestamp().toString().substring(0, 10)); // 快盘要求时间长度为10。
        params.append("&oauth_version=").append(oauth.getVersion());
        if(StringUtils.isNotEmpty(oauth.getCallback())){
            params.append("&oauth_callback=").append(oauth.getCallback());
        }
        return url.append(KuaiPanUtil.encodeUrl(params.toString())).toString();
    }
    
    public static RequestTokenRes requestToken(){
        try {
            AuthEntity oauth = new AuthEntity();
            oauth.setConsumerKey(KuaiPanGlobal.CONSUMER_KEY);
            oauth.setNonce(KuaiPanUtil.getRandomNonce(8));
            oauth.setSignatureMethod(KuaiPanGlobal.SIGNATURE_METHOD);
            oauth.setTimestamp(System.currentTimeMillis());
            oauth.setVersion(KuaiPanGlobal.VERSION);
            //oauth.setCallback("http://183.129.150.170:8080/dreambox-web/go.do");
            String url = gererateRequestUrl(oauth);
            System.out.println(url);
            String res = ThreadSafeHttpClient.get(url);
            System.out.println(res);
            return JsonUtil.parseJson2Object(res, RequestTokenRes.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String authorize(RequestTokenRes param){
        try {
            String res = ThreadSafeHttpClient.get(KuaiPanGlobal.AUTHORIZE_URL + param.getOauthToken());
            System.out.println(res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) throws Exception {
        authorize(requestToken());
    }

}
