package com.example.dragon.sc.util;

/**
 * Created by Dragon on 2017/7/18.
 */




import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
/**
 * Description:
 * <br/><a href="http://www.saichenglogistics.com">赛诚国际物流有限公司</a>
 * <br/>© 2012 Sai Cheng Logistics International Co.,Ltd
 */
public class HttpUtil
{
    // 创建HttpClient对象

    static HttpClient httpClient = getHttpClient();

    public static String BASE_URL(String ip){
        String url;
        if(ip!=null && !ip.equals("")){
            url = "http://"+ip+":9090/appServer/";
//            url = "http://"+ip+":9090/scliapp/";
        }else{
//            url = "http://10.2.1.40:8888/scliapp/android/";
            url= null;
        }
        return url;
    }
    /**
     *
     * @param url 发送请求的URL
     * @return 服务器响应字符串
     * @throws Exception
     */
    public static String getRequest(String url)
            throws Exception
    {
        // 创建HttpGet对象。
        HttpGet get = new HttpGet(url);
        // 发送GET请求
        HttpResponse httpResponse = httpClient.execute(get);
        // 如果服务器成功地返回响应

        if (httpResponse.getStatusLine().getStatusCode() == 200)
        {
            // 获取服务器响应字符串
            String result = EntityUtils.toString(httpResponse.getEntity());
            return result;
        }
        return null;
    }

    /**
     *
     * @param url 发送请求的URL
     * @param rawParams 请求参数
     * @return 服务器响应字符串
     * @throws Exception
     * @throws Exception
     */
    public static String postRequest(String url, Map<String ,String> rawParams)throws Exception
    {
        // 创建HttpPost对象。
        HttpPost post = new HttpPost(url);
        // 如果传递参数个数比较多的话可以对传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for(String key : rawParams.keySet())
        {
            //封装请求参数
            params.add(new BasicNameValuePair(key , rawParams.get(key)));
        }
        // 设置请求参数
        post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
        // 发送POST请求
        HttpResponse httpResponse = httpClient.execute(post);
        // 如果服务器成功地返回响应
        Log.d("***",String.valueOf(httpResponse.getStatusLine().getStatusCode()));
        if (httpResponse.getStatusLine().getStatusCode() == 200)
        {
            // 获取服务器响应字符串
            Log.d("***", httpResponse.getEntity().toString());
            String result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            Log.d("***", result);
            return result;
        }
        return rawParams.toString();
    }

    private static synchronized HttpClient getHttpClient() {
        HttpClient httpClient = null;
        if(httpClient == null) {
            final HttpParams httpParams = new BasicHttpParams();

            ConnManagerParams.setTimeout(httpParams, 1000);
            HttpConnectionParams.setConnectionTimeout(httpParams, 20*1000);
            HttpConnectionParams.setSoTimeout(httpParams, 20*1000);
            ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(20));
            ConnManagerParams.setMaxTotalConnections(httpParams, 20);
            HttpProtocolParams.setUseExpectContinue(httpParams, true);
            HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
            HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
            HttpClientParams.setRedirecting(httpParams, false);
            String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
            HttpProtocolParams.setUserAgent(httpParams, userAgent);
            HttpConnectionParams.setTcpNoDelay(httpParams, true);
            HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 8080));
            ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
            httpClient = new DefaultHttpClient(manager, httpParams);
        }

        return httpClient;
    }


}

