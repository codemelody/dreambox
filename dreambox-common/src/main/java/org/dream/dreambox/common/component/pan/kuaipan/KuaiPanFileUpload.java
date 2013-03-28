package org.dream.dreambox.common.component.pan.kuaipan;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dream.dreambox.common.component.pan.kuaipan.domain.AccessTokenRes;
import org.dream.dreambox.common.component.pan.kuaipan.domain.AuthEntity;
import org.dream.dreambox.common.component.pan.kuaipan.domain.UploadFileEntity;
import org.dream.dreambox.common.component.pan.kuaipan.domain.UploadLocateRes;
import org.dream.dreambox.common.component.pan.kuaipan.util.JsonUtil;
import org.dream.dreambox.common.component.pan.kuaipan.util.KuaiPanGlobal;
import org.dream.dreambox.common.component.pan.kuaipan.util.KuaiPanUtil;
import org.dream.dreambox.common.util.ThreadSafeHttpClient;

import sun.misc.BASE64Encoder;

public class KuaiPanFileUpload {

    public void uploadFile(File file, AccessTokenRes res, String uploadUrl) {
        try {
            UploadFileEntity upload = new UploadFileEntity();
            AuthEntity oauth = new AuthEntity();
            oauth.setConsumerKey(KuaiPanGlobal.CONSUMER_KEY);
            oauth.setNonce(KuaiPanUtil.getRandomNonce(8));
            oauth.setSignatureMethod(KuaiPanGlobal.SIGNATURE_METHOD);
            oauth.setTimestamp(System.currentTimeMillis());
            oauth.setVersion(KuaiPanGlobal.VERSION);
            //oauth.setToken("01aafbd751041a3a01816fcd");
            oauth.setToken(res.getOauthToken());
            upload.setAuth(oauth);
            upload.setOverwrite("True");
            upload.setRoot(KuaiPanGlobal.ROOT);
            upload.setPath("%2Ftest.txt");

            HttpClient client = ThreadSafeHttpClient.getInstance();
            String url = generateUploadFileRequestURL(upload, uploadUrl, res.getOauthTokenSecret());
            HttpPost post = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
                "----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());

            FileBody bin = new FileBody(file, "application/octet-stream");
            multipartEntity.addPart("file", bin);

            post.setEntity(multipartEntity);
            post.addHeader("Content-Type",
                "multipart/form-data; boundary=----------ThIs_Is_tHe_bouNdaRY_$");//������ʵ��Ϊ�˼��ϣ�boundary=----------ThIs_Is_tHe_bouNdaRY_$��MultipartEntity�Զ���ɵı?ֻ��ǰ�벿�֣�û�к����������һֱ˵���Ҳ���boundary....
            HttpResponse httpResponse = client.execute(post);
            System.out.println(httpResponse.getStatusLine().getStatusCode());
            HttpEntity resEntity = httpResponse.getEntity();
            System.out.println((resEntity == null) ? "" : EntityUtils.toString(resEntity, HTTP.UTF_8));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateUploadFileRequestURL(UploadFileEntity upload, String uploadUrl, String secret) {
        StringBuffer url = new StringBuffer(uploadUrl + KuaiPanGlobal.UPLOAD_FILE_SUFFIX_URL + "?");
        url.append("oauth_signature=").append(getUploadFileSignature(upload, uploadUrl, secret));
        url.append("&oauth_consumer_key=").append(upload.getAuth().getConsumerKey());
        url.append("&oauth_nonce=").append(upload.getAuth().getNonce());
        url.append("&oauth_signature_method=").append(upload.getAuth().getSignatureMethod());
        url.append("&oauth_timestamp=").append(upload.getAuth().getTimestamp().toString().substring(0, 10));
        url.append("&oauth_token=").append(upload.getAuth().getToken());
        url.append("&oauth_version=").append(upload.getAuth().getVersion());
        url.append("&overwrite=").append(upload.getOverwrite());
        url.append("&path=").append(upload.getPath());
        url.append("&root=").append(upload.getRoot());
        System.out.println(url);
        return url.toString();
    }

    private String getUploadFileSignature(UploadFileEntity upload, String uploadUrl, String tokenSecret) {
        String baseUrl = gererateUploadFileBaseUrl(upload, uploadUrl);
        String secret = KuaiPanGlobal.CONSUMER_SECRET + "&" + tokenSecret;
        String base64 = new BASE64Encoder().encode(KuaiPanUtil.encodeHmacSHA(baseUrl.getBytes(),
            secret.getBytes()));
        return base64;
    }

    private String gererateUploadFileBaseUrl(UploadFileEntity upload, String uploadUrl) {
        StringBuffer url = new StringBuffer(
            "POST&" + KuaiPanUtil.encodeUrl(uploadUrl + KuaiPanGlobal.UPLOAD_FILE_SUFFIX_URL) + "&");
        StringBuffer params = new StringBuffer();
        params.append("oauth_consumer_key=").append(upload.getAuth().getConsumerKey());
        params.append("&oauth_nonce=").append(upload.getAuth().getNonce());
        params.append("&oauth_signature_method=").append(upload.getAuth().getSignatureMethod());
        params.append("&oauth_timestamp=").append(upload.getAuth().getTimestamp().toString().substring(0, 10));
        params.append("&oauth_token=").append(upload.getAuth().getToken());
        params.append("&oauth_version=").append(upload.getAuth().getVersion());
        params.append("&overwrite=").append(upload.getOverwrite());
        params.append("&path=").append(upload.getPath());
        params.append("&root=").append(upload.getRoot());
        return url.append(KuaiPanUtil.encodeUrl(params.toString())).toString();
    }

    public UploadLocateRes uploadLocate(AccessTokenRes param) {
        try {
            UploadFileEntity upload = new UploadFileEntity();
            AuthEntity oauth = new AuthEntity();
            oauth.setConsumerKey(KuaiPanGlobal.CONSUMER_KEY);
            oauth.setNonce(KuaiPanUtil.getRandomNonce(8));
            oauth.setSignatureMethod(KuaiPanGlobal.SIGNATURE_METHOD);
            oauth.setTimestamp(System.currentTimeMillis());
            oauth.setVersion(KuaiPanGlobal.VERSION);
            oauth.setToken(param.getOauthToken());
            upload.setAuth(oauth);
            upload.setSourceIp("");
            String url = generateUploadLocateRequestURL(upload, param.getOauthTokenSecret());
            String res = ThreadSafeHttpClient.get(url);
            System.out.println(res);
            return JsonUtil.parseJson2Object(res, UploadLocateRes.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateUploadLocateRequestURL(UploadFileEntity upload, String secret) {
        StringBuffer url = new StringBuffer(KuaiPanGlobal.UPLOAD_LOCATE_URL + "?");
        url.append("oauth_consumer_key=").append(upload.getAuth().getConsumerKey());
        url.append("&oauth_token=").append(upload.getAuth().getToken());
        url.append("&oauth_signature_method=").append(upload.getAuth().getSignatureMethod());
        url.append("&oauth_signature=").append(getUploadLocateSignature(upload, secret));
        url.append("&oauth_timestamp=").append(
            upload.getAuth().getTimestamp().toString().substring(0, 10));
        url.append("&oauth_nonce=").append(upload.getAuth().getNonce());
        url.append("&oauth_version=").append(upload.getAuth().getVersion());
        if (StringUtils.isNotEmpty(upload.getSourceIp())) {
            url.append("&source_ip=").append(upload.getSourceIp());
        }
        return url.toString();
    }

    private String getUploadLocateSignature(UploadFileEntity upload, String tokenSecret) {
        String baseUrl = gererateUploadLocateBaseUrl(upload);
        String secret = KuaiPanGlobal.CONSUMER_SECRET + "&" + tokenSecret;
        String base64 = new BASE64Encoder().encode(KuaiPanUtil.encodeHmacSHA(baseUrl.getBytes(),
            secret.getBytes()));
        return base64;
    }

    private String gererateUploadLocateBaseUrl(UploadFileEntity upload) {
        StringBuffer url = new StringBuffer(
            "GET&" + KuaiPanUtil.encodeUrl(KuaiPanGlobal.UPLOAD_LOCATE_URL) + "&");
        StringBuffer params = new StringBuffer();
        params.append("oauth_consumer_key=").append(upload.getAuth().getConsumerKey());
        params.append("&oauth_token").append(upload.getAuth().getToken());
        params.append("&oauth_signature_method=").append(upload.getAuth().getSignatureMethod());
        params.append("&oauth_timestamp=").append(
            upload.getAuth().getTimestamp().toString().substring(0, 10));
        params.append("&oauth_nonce=").append(upload.getAuth().getNonce());
        params.append("&oauth_version=").append(upload.getAuth().getVersion());
        if (StringUtils.isNotEmpty(upload.getSourceIp())) {
            params.append("&source_ip=").append(upload.getSourceIp());
        }
        return url.append(KuaiPanUtil.encodeUrl(params.toString())).toString();
    }

    public static void main(String[] args) {
        //uploadLocate();
        //KuaiPanOAuth.requestToken();
        //uploadFile(new File("f:/a.txt"), );
        
    }
}
