package org.dream.dreambox.common.component.pan.kuaipan;

import org.apache.commons.lang.StringUtils;
import org.dream.dreambox.common.component.pan.kuaipan.domain.AccessTokenRes;
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
     * @param oauth
     * @return
     */
    private static String generateRequestUrl(AuthEntity oauth){
        if(StringUtils.isEmpty(oauth.getConsumerKey()) || oauth.getTimestamp() == null 
                || oauth.getTimestamp() == 0 || StringUtils.isEmpty(oauth.getNonce())){
            throw new RuntimeException("参数异常[consumerKey=" + oauth.getConsumerKey() 
                + ", timestamp=" + oauth.getTimestamp() + ", nonce=" + oauth.getNonce() + "]");
        }
        StringBuffer url = new StringBuffer(KuaiPanGlobal.REQUEST_TOKEN_URL + "?");
        url.append("oauth_signature=").append(generateRequestTokenSignature(oauth));
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
     * 生成RequestToken签名。
     * @param oauth
     * @return
     */
    private static String generateRequestTokenSignature(AuthEntity oauth) {
        String baseUrl = generateRequestTokenBaseUrl(oauth);
        String secret = KuaiPanGlobal.CONSUMER_SECRET + "&";
        String base64 = new BASE64Encoder().encode(
            KuaiPanUtil.encodeHmacSHA(baseUrl.getBytes(), secret.getBytes()));
        return base64;
    }

    /**
     * 生成基本的URL，即需要加密的基础串，不含signature的URL
     * @param oauth
     * @return
     */
    private static String generateRequestTokenBaseUrl(AuthEntity oauth){
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
    
    /**
     * 获取未授权的临时 token。
     * @return
     */
    public static RequestTokenRes requestToken(){
        try {
            AuthEntity oauth = new AuthEntity();
            oauth.setConsumerKey(KuaiPanGlobal.CONSUMER_KEY);
            oauth.setNonce(KuaiPanUtil.getRandomNonce(8));
            oauth.setSignatureMethod(KuaiPanGlobal.SIGNATURE_METHOD);
            oauth.setTimestamp(System.currentTimeMillis());
            oauth.setVersion(KuaiPanGlobal.VERSION);
            //oauth.setCallback("http://183.129.150.170:8080/dreambox-web/go.do");
            String url = generateRequestUrl(oauth);
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
            System.out.println(KuaiPanGlobal.AUTHORIZE_URL + param.getOauthToken());
            String res = ThreadSafeHttpClient.get(KuaiPanGlobal.AUTHORIZE_URL + param.getOauthToken());
            System.out.println(res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 用临时 token 换取 access token。
     * @param token 临时 token。
     * @param toeknSecret 对应的secret。
     * @param verifer 验证码
     * @return 包含已经授权的token的AccessTokenRes对象
     */
    public static AccessTokenRes accessToken(RequestTokenRes param, String verifer){
        try{
            AuthEntity oauth = new AuthEntity();
            oauth.setConsumerKey(KuaiPanGlobal.CONSUMER_KEY);
            oauth.setSignatureMethod(KuaiPanGlobal.SIGNATURE_METHOD);
            oauth.setTimestamp(System.currentTimeMillis());
            oauth.setNonce(KuaiPanUtil.getRandomNonce(8));
            oauth.setVersion(KuaiPanGlobal.VERSION);
            oauth.setToken(param.getOauthToken());
            String url = generateAccessTokenRequestUrl(oauth, param.getOauthTokenSecret(), verifer);
            String json = ThreadSafeHttpClient.get(url);
            System.out.println(json);
            return JsonUtil.parseJson2Object(json, AccessTokenRes.class);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
    }
    
    /**
     * 生成accessToken的请求URL
     * @param oauth 基本参数对象。
     * @param verifier 验证码。
     * @return
     */
    private static String generateAccessTokenRequestUrl(AuthEntity oauth, String tokenSecret, String verifier){
        try{
            if(StringUtils.isEmpty(oauth.getConsumerKey()) || oauth.getTimestamp() == null 
                    || oauth.getTimestamp() == 0 || StringUtils.isEmpty(oauth.getNonce()) 
                    || StringUtils.isEmpty(oauth.getToken())){
                throw new RuntimeException("参数异常[consumerKey=" + oauth.getConsumerKey() 
                    + ", timestamp=" + oauth.getTimestamp() + ", nonce=" + oauth.getNonce() 
                    + ", token=" + oauth.getToken() + "]");
            }
            StringBuffer url = new StringBuffer(KuaiPanGlobal.ACCESS_TOKEN_URL + "?");
            url.append("oauth_signature=").append(generateAccessTokenSignature(oauth, tokenSecret, verifier));
            url.append("&oauth_consumer_key=").append(oauth.getConsumerKey());
            url.append("&oauth_nonce=").append(oauth.getNonce());
            url.append("&oauth_signature_method=").append(oauth.getSignatureMethod());
            url.append("&oauth_timestamp=").append(oauth.getTimestamp().toString().substring(0, 10));
            url.append("&oauth_token=").append(oauth.getToken());
            url.append("&oauth_verifier=").append(verifier);
            url.append("&oauth_version=").append(oauth.getVersion());
            return url.toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 生成accessToken的URL签名
     * @param oauth
     * @param verifier
     * @return
     */
    private static String generateAccessTokenSignature(AuthEntity oauth, String tokenSecret, String verifier){
        String baseUrl = generateAccessTokenBaseUrl(oauth, verifier);
        String secret = KuaiPanGlobal.CONSUMER_SECRET + "&" + tokenSecret;
        System.out.println(secret);
        String base64 = new BASE64Encoder().encode(
            KuaiPanUtil.encodeHmacSHA(baseUrl.getBytes(), secret.getBytes()));
        return base64;
    }
    
    /**
     * 生成accessToken的基本URL
     * @param oauth
     * @param verifier
     * @return
     */
    private static String generateAccessTokenBaseUrl(AuthEntity oauth, String verifier){
        try{
            if(StringUtils.isEmpty(oauth.getConsumerKey()) || oauth.getTimestamp() == null 
                    || oauth.getTimestamp() == 0 || StringUtils.isEmpty(oauth.getNonce()) 
                    || StringUtils.isEmpty(oauth.getToken())){
                throw new RuntimeException("参数异常[consumerKey=" + oauth.getConsumerKey() 
                    + ", timestamp=" + oauth.getTimestamp() + ", nonce=" + oauth.getNonce() 
                    + ", token=" + oauth.getToken() + "]");
            }
            StringBuffer url = new StringBuffer("GET&" + KuaiPanUtil.encodeUrl(KuaiPanGlobal.ACCESS_TOKEN_URL) + "&");
            StringBuffer params = new StringBuffer();
            params.append("oauth_consumer_key=").append(oauth.getConsumerKey());
            params.append("&oauth_nonce=").append(oauth.getNonce());
            params.append("&oauth_signature_method=").append(oauth.getSignatureMethod());
            params.append("&oauth_timestamp=").append(oauth.getTimestamp().toString().substring(0, 10));
            params.append("&oauth_token=").append(oauth.getToken());
            params.append("&oauth_verifier=").append(verifier);
            params.append("&oauth_version=").append(oauth.getVersion());
            return url.append(KuaiPanUtil.encodeUrl(params.toString())).toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) throws Exception {
        authorize(requestToken());
    }

}
