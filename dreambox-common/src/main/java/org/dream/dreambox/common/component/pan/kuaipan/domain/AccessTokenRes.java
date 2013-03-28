package org.dream.dreambox.common.component.pan.kuaipan.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class AccessTokenRes {
    
    /** oauth_token Y   string  已经授权的 token */
    @JsonProperty(value="oauth_token")
    private String oauthToken;
    
    /** oauth_token_secret  Y   string  对应 secret */
    @JsonProperty(value="oauth_token_secret")
    private String oauthTokenSecret;
    
    /** charged_dir N   string  可见的根目录 ID */
    @JsonProperty(value="charged_dir")
    private String chargedDir;
    
    /** user_id Y   string  用户id */
    @JsonProperty(value="user_id")
    private String userId;

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

    public String getChargedDir() {
        return chargedDir;
    }

    public void setChargedDir(String chargedDir) {
        this.chargedDir = chargedDir;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
