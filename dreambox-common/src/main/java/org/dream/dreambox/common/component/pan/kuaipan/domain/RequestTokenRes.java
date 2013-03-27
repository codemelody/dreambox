package org.dream.dreambox.common.component.pan.kuaipan.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class RequestTokenRes {

    private String msg;
    
    @JsonProperty(value="basestring")
    private String baseString;
    
    /** oauth_token Y   string  未授权的token */
    @JsonProperty(value="oauth_token")
    private String oauthToken;
    
    /** oauth_token_secret  Y   string  对应secret */
    @JsonProperty(value="oauth_token_secret")
    private String oauthTokenSecret;
    
    /** oauth_callback_confirmed    Y   JSON boolean    True/False，callback是否接收 */
    @JsonProperty(value="oauth_callback_confirmed")
    private String oauthCallbackConfirmed;
    
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getBaseString() {
        return baseString;
    }
    public void setBaseString(String baseString) {
        this.baseString = baseString;
    }
    public String getOauthToken() {
        return oauthToken;
    }
    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }
    public String getOauthTokenSecret() {
        return oauthTokenSecret;
    }
    public void setOauthTokenSecret(String oauthTokenSecret) {
        this.oauthTokenSecret = oauthTokenSecret;
    }
    public String getOauthCallbackConfirmed() {
        return oauthCallbackConfirmed;
    }
    public void setOauthCallbackConfirmed(String oauthCallbackConfirmed) {
        this.oauthCallbackConfirmed = oauthCallbackConfirmed;
    }
    
    
}
