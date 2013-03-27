package org.dream.dreambox.common.component.pan.kuaipan.domain;

public class AuthEntity {

    /**
     * consumer_key
     */
    private String consumerKey;
    
    /**
     * access_token
     */
    private String token;
    
    /**
     * 请填"HMAC-SHA1"
     */
    private String signatureMethod;
    
    /**
     * 本次请求的签名
     */
    private String signature;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * [0-9A-Za-z]随机字符串，长度介于16-32字节。每次请求请使用不同的nonce
     */
    private String nonce;
    
    /**
     * 版本，填写"1.0"
     */
    private String version;
    
    /**
     * 回调函数
     */
    private String callback;

    public AuthEntity() {
        super();
    }

    
    /**
     *@param consumerKey
     *@param token
     *@param signatureMethod
     *@param signature
     *@param timestamp
     *@param nonce
     *@param version
     *@param callback
     */
    public AuthEntity(String consumerKey, String token, String signatureMethod, String signature,
                 Long timestamp, String nonce, String version, String callback) {
        super();
        this.consumerKey = consumerKey;
        this.token = token;
        this.signatureMethod = signatureMethod;
        this.signature = signature;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.version = version;
        this.callback = callback;
    }


    public String getConsumerKey() {
        return consumerKey;
    }


    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }


    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }


    public String getSignatureMethod() {
        return signatureMethod;
    }


    public void setSignatureMethod(String signatureMethod) {
        this.signatureMethod = signatureMethod;
    }


    public String getSignature() {
        return signature;
    }


    public void setSignature(String signature) {
        this.signature = signature;
    }


    public Long getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


    public String getNonce() {
        return nonce;
    }


    public void setNonce(String nonce) {
        this.nonce = nonce;
    }


    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }


    public String getCallback() {
        return callback;
    }


    public void setCallback(String callback) {
        this.callback = callback;
    }
    
}
