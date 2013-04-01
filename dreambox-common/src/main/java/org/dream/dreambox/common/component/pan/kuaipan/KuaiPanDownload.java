package org.dream.dreambox.common.component.pan.kuaipan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.dream.dreambox.common.component.pan.kuaipan.domain.AccessTokenRes;
import org.dream.dreambox.common.component.pan.kuaipan.domain.AuthEntity;
import org.dream.dreambox.common.component.pan.kuaipan.domain.DownloadFileEntity;
import org.dream.dreambox.common.component.pan.kuaipan.util.KuaiPanGlobal;
import org.dream.dreambox.common.component.pan.kuaipan.util.KuaiPanUtil;
import org.dream.dreambox.common.util.ThreadSafeHttpClient;

import sun.misc.BASE64Encoder;

public class KuaiPanDownload {

    /**
     * 下载文件
     * @param res token对。
     * @param path 快盘上的路径。
     * @return
     */
    public File downloadFile(AccessTokenRes res, String localPath, String remotePath){
        PrintWriter out = null;
        try{
            DownloadFileEntity download = new DownloadFileEntity();
            AuthEntity oauth = new AuthEntity();
            oauth.setConsumerKey(KuaiPanGlobal.CONSUMER_KEY);
            oauth.setNonce(KuaiPanUtil.getRandomNonce(8));
            oauth.setSignatureMethod(KuaiPanGlobal.SIGNATURE_METHOD);
            oauth.setTimestamp(System.currentTimeMillis());
            oauth.setVersion(KuaiPanGlobal.VERSION);
            oauth.setToken(res.getOauthToken());
            download.setAuth(oauth);
            download.setPath(remotePath);
            download.setRoot(KuaiPanGlobal.ROOT);
            String url = generateDownloadFileRequestURL(download, res.getOauthTokenSecret());
            String content = ThreadSafeHttpClient.get(url);
            System.out.println(content);
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(localPath)));
            out.print(content);
            out.flush();
            //return JsonUtil.parseJson2Object(json, DownloadFileRes.class);
            return new File(localPath);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            if(out != null){
                out.close();
            }
        }
    }
    
    private String generateDownloadFileRequestURL(DownloadFileEntity download, String secret){
        StringBuffer url = new StringBuffer(KuaiPanGlobal.DOWNLOAD_FILE_URL + "?");
        url.append("oauth_signature=").append(getDownloadFileSignature(download, secret));
        url.append("&oauth_consumer_key=").append(download.getAuth().getConsumerKey());
        url.append("&oauth_nonce=").append(download.getAuth().getNonce());
        url.append("&oauth_signature_method=").append(download.getAuth().getSignatureMethod());
        url.append("&oauth_timestamp=").append(download.getAuth().getTimestamp().toString().substring(0, 10));
        url.append("&oauth_token=").append(download.getAuth().getToken());
        url.append("&oauth_version=").append(download.getAuth().getVersion());
        url.append("&path=").append(KuaiPanUtil.encodeUrl(download.getPath()));
        url.append("&root=").append(download.getRoot());
        System.out.println(url);
        return url.toString();
    }
    
    private String getDownloadFileSignature(DownloadFileEntity download, String tokenSecret){
        String baseUrl = gererateDownloadFileBaseUrl(download);
        String secret = KuaiPanGlobal.CONSUMER_SECRET + "&" + tokenSecret;
        String base64 = new BASE64Encoder().encode(KuaiPanUtil.encodeHmacSHA(baseUrl.getBytes(),
            secret.getBytes()));
        return KuaiPanUtil.encodeUrl(base64);
    }
    
    private String gererateDownloadFileBaseUrl(DownloadFileEntity download){
        StringBuffer url = new StringBuffer("GET&" + KuaiPanUtil.encodeUrl(KuaiPanGlobal.DOWNLOAD_FILE_URL) + "&");
        StringBuffer params = new StringBuffer();
        params.append("oauth_consumer_key=").append(download.getAuth().getConsumerKey());
        params.append("&oauth_nonce=").append(download.getAuth().getNonce());
        params.append("&oauth_signature_method=").append(download.getAuth().getSignatureMethod());
        params.append("&oauth_timestamp=").append(download.getAuth().getTimestamp().toString().substring(0, 10));
        params.append("&oauth_token=").append(download.getAuth().getToken());
        params.append("&oauth_version=").append(download.getAuth().getVersion());
        params.append("&path=").append(KuaiPanUtil.encodeUrl(download.getPath()));
        params.append("&root=").append(download.getRoot());
        return url.append(KuaiPanUtil.encodeUrl(params.toString())).toString();
    }
}
