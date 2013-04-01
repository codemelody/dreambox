package org.dream.dreambox.common.util;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class ThreadSafeHttpClient {
    
    /**
     * HTTP协议的默认端口号。
     */
    private static final int HTTP_PORT = 80;
    
    /**
     * HTTPS协议的默认端口号。
     */
    private static final int HTTPS_PORT = 433;
    
    /**
     * 超时时间
     */
    private static final int TIME_OUT = 20000;
    
    /**
     * 默认字符集为UTF-8。
     */
    private static final String CHARSET = HTTP.UTF_8;
    
    /**
     * HttpClient单实例。
     */
    private static HttpClient httpClient;
    
    /*
     * 私有化构造方法。
     */
    private ThreadSafeHttpClient(){}
    
    /**
     * 同步取得单实例的<code>{@link HttpClient}</code>对象。
     * @return 实例化后的<code>{@link HttpClient}</code>对象。
     */
    public static synchronized HttpClient getInstance(){
        if(httpClient == null){
            HttpParams params = new BasicHttpParams();
            // 设置一些基本参数
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, true);
            HttpProtocolParams.setUserAgent(params, 
                "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
            
            // 超时设置
            ConnManagerParams.setTimeout(params, TIME_OUT); // 从连接池中取连接的超时时间
            HttpConnectionParams.setConnectionTimeout(params, TIME_OUT); // 连接超时
            HttpConnectionParams.setSoTimeout(params, TIME_OUT); // 请求超时
            
            httpClient = new DefaultHttpClient(params);
        }
        return httpClient;
    }
    
    /**
     * 利用HttpClient发送POST请求。
     * @param url URL路径。
     * @param params POST请求所携带的参数。若无参数，可传入<code>null</code>。
     * @return HttpClient请求后的响应值。
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public static String post(String url, List<NameValuePair> params) throws ClientProtocolException, IOException {
        // 创建POST请求
        HttpPost httpPost = new HttpPost(url);
        if(params != null && params.size() > 0){
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, CHARSET);
            httpPost.setEntity(entity);
        }
        HttpClient client = getInstance();
        // 发送请求
        HttpResponse response = client.execute(httpPost);
        /*if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("请求失败");
        }*/
        HttpEntity resEntity =  response.getEntity();
        return (resEntity == null) ? "" : EntityUtils.toString(resEntity, CHARSET);
    }
    
    /**
     * 利用HttpClient发送GET请求。
     * @param url URL路径。
     * @return HttpClient请求后的响应值。
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public static String get(String url) throws ClientProtocolException, IOException{
        httpClient = getInstance();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        /*if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("请求失败");
        }*/
        System.out.println(response.getStatusLine().getStatusCode());
        HttpEntity resEntity = response.getEntity();
        return (resEntity == null) ? "" : EntityUtils.toString(resEntity, CHARSET);
    }

}
