package org.dream.dreambox.common.component.pan.kuaipan;

import org.dream.dreambox.common.component.pan.kuaipan.domain.AccessTokenRes;
import org.dream.dreambox.common.component.pan.kuaipan.domain.AuthEntity;
import org.dream.dreambox.common.component.pan.kuaipan.domain.FileInfoEntity;
import org.dream.dreambox.common.component.pan.kuaipan.domain.FileInfoRes;
import org.dream.dreambox.common.component.pan.kuaipan.util.JsonUtil;
import org.dream.dreambox.common.component.pan.kuaipan.util.KuaiPanGlobal;
import org.dream.dreambox.common.component.pan.kuaipan.util.KuaiPanUtil;
import org.dream.dreambox.common.util.ThreadSafeHttpClient;

import sun.misc.BASE64Encoder;

public class KuaiPanFileInfo {

    public FileInfoRes getMetadata(AccessTokenRes accessToken){
        try{
            FileInfoEntity info = new FileInfoEntity();
            AuthEntity oauth = new AuthEntity();
            oauth.setConsumerKey(KuaiPanGlobal.CONSUMER_KEY);
            oauth.setNonce(KuaiPanUtil.getRandomNonce(8));
            oauth.setSignatureMethod(KuaiPanGlobal.SIGNATURE_METHOD);
            oauth.setTimestamp(System.currentTimeMillis());
            oauth.setVersion(KuaiPanGlobal.VERSION);
            oauth.setToken(accessToken.getOauthToken());
            info.setAuth(oauth);
            String url = generateFileInfoRequestURL(info, accessToken.getOauthTokenSecret());
            String json = ThreadSafeHttpClient.get(url);
            System.out.println(json);
            return JsonUtil.parseJson2Object(json, FileInfoRes.class);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String generateFileInfoRequestURL(FileInfoEntity info, String oauthTokenSecret) {
        StringBuffer url = new StringBuffer(KuaiPanGlobal.METADATA_URL + KuaiPanGlobal.ROOT + "?");
        url.append("oauth_signature=").append(getMetadataSignature(info, oauthTokenSecret));
        url.append("&oauth_consumer_key=").append(info.getAuth().getConsumerKey());
        url.append("&oauth_nonce=").append(info.getAuth().getNonce());
        url.append("&oauth_signature_method=").append(info.getAuth().getSignatureMethod());
        url.append("&oauth_timestamp=").append(info.getAuth().getTimestamp().toString().substring(0, 10));
        url.append("&oauth_token=").append(info.getAuth().getToken());
        url.append("&oauth_version=").append(info.getAuth().getVersion());
        return url.toString();
    }

    private String getMetadataSignature(FileInfoEntity info, String oauthTokenSecret) {
        String baseUrl = gererateFileInfoBaseUrl(info);
        String secret = KuaiPanGlobal.CONSUMER_SECRET + "&" + oauthTokenSecret;
        String base64 = new BASE64Encoder().encode(KuaiPanUtil.encodeHmacSHA(baseUrl.getBytes(),
            secret.getBytes()));
        return base64;
    }

    private String gererateFileInfoBaseUrl(FileInfoEntity info) {
        StringBuffer url = new StringBuffer("GET&" + KuaiPanUtil.encodeUrl(KuaiPanGlobal.METADATA_URL + KuaiPanGlobal.ROOT) + "&");
        StringBuffer params = new StringBuffer();
        params.append("oauth_consumer_key=").append(info.getAuth().getConsumerKey());
        params.append("&oauth_nonce=").append(info.getAuth().getNonce());
        params.append("&oauth_signature_method=").append(info.getAuth().getSignatureMethod());
        params.append("&oauth_timestamp=").append(info.getAuth().getTimestamp().toString().substring(0, 10));
        params.append("&oauth_token").append(info.getAuth().getToken());
        params.append("&oauth_version=").append(info.getAuth().getVersion());
        return url.append(KuaiPanUtil.encodeUrl(params.toString())).toString();
    }
}
