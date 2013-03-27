package org.dream.dreambox.common.component.pan.kuaipan.util;


/**
 * ��ɽ����API��URL��
 * @author </a href="mailto:dingjun@llyuntong.com.cn">dingjun</a>
 * @version $Id: KuaiPanURL.java, v 0.1 2013-2-21 ����10:50:02 Administrator Exp $ 
 * @since 1.0
 */
public class KuaiPanGlobal {

    public static final String CONSUMER_KEY = "xclbDQjBhOytQbXY";
    public static final String CONSUMER_SECRET = "cX3e8OZQowajdonq";
    public static final String SIGNATURE_METHOD = "HMAC-SHA1";
    public static final String VERSION = "1.0";
    public static final String ROOT = "app_folder";
    
    public static final String REQUEST_TOKEN_URL = "https://openapi.kuaipan.cn/open/requestToken";
    public static final String AUTHORIZE_URL     = "https://www.kuaipan.cn/api.php?ac=open&op=authorise&oauth_token=";
    public static final String ACCESS_TOKEN_URL  = "https://openapi.kuaipan.cn/open/accessToken";
    
    public static final String UPLOAD_LOCATE_URL      = "http://api-content.dfs.kuaipan.cn/1/fileops/upload_locate";
    public static final String UPLOAD_FILE_SUFFIX_URL = "1/fileops/upload_file";
    
}
