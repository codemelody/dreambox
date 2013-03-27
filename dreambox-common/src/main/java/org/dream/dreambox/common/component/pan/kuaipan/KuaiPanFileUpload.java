package org.dream.dreambox.common.component.pan.kuaipan;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.dream.dreambox.common.component.pan.kuaipan.domain.AuthEntity;
import org.dream.dreambox.common.component.pan.kuaipan.domain.UploadFileEntity;
import org.dream.dreambox.common.component.pan.kuaipan.util.KuaiPanGlobal;
import org.dream.dreambox.common.component.pan.kuaipan.util.KuaiPanUtil;
import org.dream.dreambox.common.util.ThreadSafeHttpClient;

import sun.misc.BASE64Encoder;

public class KuaiPanFileUpload {

    public static void uploadFile(File file) {
        try {
            UploadFileEntity upload = new UploadFileEntity();
            AuthEntity oauth = new AuthEntity();
            oauth.setConsumerKey(KuaiPanGlobal.CONSUMER_KEY);
            oauth.setNonce(KuaiPanUtil.getRandomNonce(8));
            oauth.setSignatureMethod(KuaiPanGlobal.SIGNATURE_METHOD);
            oauth.setTimestamp(System.currentTimeMillis());
            oauth.setVersion(KuaiPanGlobal.VERSION);
            oauth.setToken("581b10031af74acc92e6cb6145f6c856");
            upload.setAuth(oauth);
            upload.setOverwrite("True");
            upload.setRoot(KuaiPanGlobal.ROOT);
            upload.setPath("%2Ftest.txt");

            HttpClient client = ThreadSafeHttpClient.getInstance();
            String url = generateUploadFileRequestURL(upload, "http://p4.dfs.kuaipan.cn/cdlnode/");
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
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateUploadFileRequestURL(UploadFileEntity upload, String uploadUrl) {
        StringBuffer url = new StringBuffer(uploadUrl + KuaiPanGlobal.UPLOAD_FILE_SUFFIX_URL + "?");
        url.append("oauth_consumer_key=").append(upload.getAuth().getConsumerKey());
        url.append("&oauth_token=").append(upload.getAuth().getToken());
        url.append("&oauth_signature_method=").append(upload.getAuth().getSignatureMethod());
        url.append("&oauth_signature=").append(getUploadFileSignature(upload));
        url.append("&oauth_timestamp=").append(
            upload.getAuth().getTimestamp().toString().substring(0, 10));
        url.append("&oauth_nonce=").append(upload.getAuth().getNonce());
        url.append("&oauth_version=").append(upload.getAuth().getVersion());
        url.append("overwrite=").append(upload.getOverwrite());
        url.append("root=").append(upload.getRoot());
        url.append("path=").append(upload.getPath());
        System.out.println(url);
        return url.toString();
    }

    private static String getUploadFileSignature(UploadFileEntity upload) {
        String baseUrl = gererateUploadFileBaseUrl(upload);
        String secret = KuaiPanGlobal.CONSUMER_SECRET + "&" + "73608d2da1e74b76807b419e8f4e4b49";
        String base64 = new BASE64Encoder().encode(KuaiPanUtil.encodeHmacSHA(baseUrl.getBytes(),
            secret.getBytes()));
        return base64;
    }

    private static String gererateUploadFileBaseUrl(UploadFileEntity upload) {
        StringBuffer url = new StringBuffer(
            "POST&" + KuaiPanUtil.encodeUrl(KuaiPanGlobal.UPLOAD_LOCATE_URL) + "&");
        StringBuffer params = new StringBuffer();
        params.append("oauth_consumer_key=").append(upload.getAuth().getConsumerKey());
        params.append("&oauth_token=").append(upload.getAuth().getToken());
        params.append("&oauth_signature_method=").append(upload.getAuth().getSignatureMethod());
        params.append("&oauth_timestamp=").append(
            upload.getAuth().getTimestamp().toString().substring(0, 10));
        params.append("&oauth_nonce=").append(upload.getAuth().getNonce());
        params.append("&oauth_version=").append(upload.getAuth().getVersion());
        params.append("overwrite=").append(upload.getOverwrite());
        params.append("root=").append(upload.getRoot());
        params.append("path=").append(upload.getPath());
        return url.append(KuaiPanUtil.encodeUrl(params.toString())).toString();
    }

    public static void uploadLocate() {
        try {
            UploadFileEntity upload = new UploadFileEntity();
            AuthEntity oauth = new AuthEntity();
            oauth.setConsumerKey(KuaiPanGlobal.CONSUMER_KEY);
            oauth.setNonce(KuaiPanUtil.getRandomNonce(8));
            oauth.setSignatureMethod(KuaiPanGlobal.SIGNATURE_METHOD);
            oauth.setTimestamp(System.currentTimeMillis());
            oauth.setVersion(KuaiPanGlobal.VERSION);
            oauth.setToken("0d4e42033cfc4b71bcd050f5ce7af1fe");
            upload.setAuth(oauth);
            upload.setSourceIp("");
            String url = generateUploadLocateRequestURL(upload);
            String res = ThreadSafeHttpClient.get(url);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateUploadLocateRequestURL(UploadFileEntity upload) {
        StringBuffer url = new StringBuffer(KuaiPanGlobal.UPLOAD_LOCATE_URL + "?");
        url.append("oauth_consumer_key=").append(upload.getAuth().getConsumerKey());
        url.append("&oauth_token=").append(upload.getAuth().getToken());
        url.append("&oauth_signature_method=").append(upload.getAuth().getSignatureMethod());
        url.append("&oauth_signature=").append(getUploadLocateSignature(upload));
        url.append("&oauth_timestamp=").append(
            upload.getAuth().getTimestamp().toString().substring(0, 10));
        url.append("&oauth_nonce=").append(upload.getAuth().getNonce());
        url.append("&oauth_version=").append(upload.getAuth().getVersion());
        if (StringUtils.isNotEmpty(upload.getSourceIp())) {
            url.append("&source_ip=").append(upload.getSourceIp());
        }
        return url.toString();
    }

    private static String getUploadLocateSignature(UploadFileEntity upload) {
        String baseUrl = gererateUploadLocateBaseUrl(upload);
        String secret = KuaiPanGlobal.CONSUMER_SECRET + "&" + "73608d2da1e74b76807b419e8f4e4b49";
        String base64 = new BASE64Encoder().encode(KuaiPanUtil.encodeHmacSHA(baseUrl.getBytes(),
            secret.getBytes()));
        return base64;
    }

    private static String gererateUploadLocateBaseUrl(UploadFileEntity upload) {
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
        KuaiPanOAuth.requestToken();
        uploadFile(new File("f:/a.txt"));
        
    }
}
