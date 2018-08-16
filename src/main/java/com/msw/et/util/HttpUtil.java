package com.msw.et.util;

import net.sf.json.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by iBoy on 2017/4/20.
 */
public class HttpUtil {
    public HttpUtil() {
    }

    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 20000;   // 20秒超时
    private static final String ENCODING = "UTF-8";

    static {
        // 设置连接池
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(1000);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
        requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(MAX_TIMEOUT)   // 设置从连接池获取连接实例的超时
                .setConnectTimeout(MAX_TIMEOUT)             // 设置连接超时
                .setSocketTimeout(MAX_TIMEOUT)              // 设置读取超时
                .setCookieSpec(CookieSpecs.STANDARD_STRICT) // Cookie策略
                .setRedirectsEnabled(true)
                .setRelativeRedirectsAllowed(true)
                .build();
    }

    /**
     * 产生通用的HttpClient对象
     */
    public static CloseableHttpClient getHttpClient() {
        final CloseableHttpClient httpclient;
        httpclient = HttpClients.custom().build();
        return httpclient;
    }

    /**
     * 创建HTTPS的HttpClinet对象
     */
    public static CloseableHttpClient getSSLHttpClient() {
        SSLContext sslContext = null;
        //忽略证书校验
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        return HttpClients.custom()
                .setConnectionManager(connMgr)
                .setSSLHostnameVerifier(hostnameVerifier)
                .build();
    }

    public static CloseableHttpResponse doGet(String url, CloseableHttpClient httpclient) {
        System.out.println("接口url: " + url);
        HttpGet http = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(http);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static CloseableHttpResponse doPost(String url, JSONObject jsonObject, CloseableHttpClient httpclient) {
        System.out.println("接口url: " + url);
        HttpPost http = new HttpPost(url);
        CloseableHttpResponse response = null;
        http.setHeader("Content-Type", "application/json;charset=UTF-8");
        http.setHeader("Accept", "application/json");
        if (jsonObject != null) {
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            http.setEntity(entity);
        }
        try {
            response = httpclient.execute(http);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static CloseableHttpResponse doPostWithJsonString(String url, String param, CloseableHttpClient httpclient) {
        System.out.println("接口url: " + url);
        HttpPost http = new HttpPost(url);
        CloseableHttpResponse response = null;
        http.setHeader("Content-Type", "application/json;charset=UTF-8");
        http.setHeader("Accept", "application/json");
        if (param != null) {
            StringEntity entity = new StringEntity(param, "UTF-8");
            http.setEntity(entity);
        }
        try {
            response = httpclient.execute(http);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static CloseableHttpResponse doPostWithForm(Map<String, String> map, String url, CloseableHttpClient httpclient) {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();//用于存放表单数据.

        //遍历map 将其中的数据转化为表单数据
        for (Map.Entry<String, String> entry : map.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            //对表单数据进行url编码
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs);
            httpPost.setEntity(urlEncodedFormEntity);
            HttpClientContext httpClientContext = new HttpClientContext();
            CloseableHttpResponse response = httpclient.execute(httpPost, httpClientContext);//发送数据.提交表单
            CookieStore cookieStore = httpClientContext.getCookieStore(); //获取cookie 第一步
            List<Cookie> cookies = cookieStore.getCookies();  //获取所有的cookie
            System.out.println("cookies.size" + cookies.size());
            for (Cookie cookie : cookies) {
                System.out.println("name=" + cookie.getName() + "====value=" + cookie.getValue());
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static CloseableHttpResponse doDelete(String url, CloseableHttpClient httpclient) {
        System.out.println("接口url: " + url);
        HttpDelete http = new HttpDelete(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(http);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    public static CloseableHttpResponse doPut(String url, JSONObject jsonObject, CloseableHttpClient httpclient) {
        System.out.println("接口url: " + url);
        HttpPut http = new HttpPut(url);
        CloseableHttpResponse response = null;
        http.setHeader("Content-Type", "application/json;charset=UTF-8");
        http.setHeader("Accept", "application/json");
        if (jsonObject != null) {
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            http.setEntity(entity);
        }
        try {
            response = httpclient.execute(http);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static CloseableHttpResponse doPutWithJsonString(String url, String param, CloseableHttpClient httpclient) {
        System.out.println("接口url: " + url);
        HttpPut http = new HttpPut(url);
        CloseableHttpResponse response = null;
        http.setHeader("Content-Type", "application/json;charset=UTF-8");
        http.setHeader("Accept", "application/json");
        if (param != null) {
            StringEntity entity = new StringEntity(param, "UTF-8");
            http.setEntity(entity);
        }
        try {
            response = httpclient.execute(http);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static CloseableHttpResponse doPutWithForm(Map<String, String> map, String url, CloseableHttpClient httpclient) {
        HttpPut httpPut = new HttpPut(url);
        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();//用于存放表单数据.

        //遍历map 将其中的数据转化为表单数据
        for (Map.Entry<String, String> entry : map.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            //对表单数据进行url编码
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs);
            httpPut.setEntity(urlEncodedFormEntity);
            HttpClientContext httpClientContext = new HttpClientContext();
            CloseableHttpResponse response = httpclient.execute(httpPut, httpClientContext);//发送数据.提交表单
            CookieStore cookieStore = httpClientContext.getCookieStore(); //获取cookie 第一步
            List<Cookie> cookies = cookieStore.getCookies();  //获取所有的cookie
            System.out.println("cookies.size" + cookies.size());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param url:接口请求URL
     * @param jsonParam：key为参数名，value参数值
     * @param filesObject：               key为上传的文件参数名包括后缀，value为文件的名字
     * @param httpclient                 注： 上传的文件放在resources目录下的files目录
     * @return
     */
    public static CloseableHttpResponse doPostUpfile(String url, JSONObject jsonParam, JSONObject filesObject, CloseableHttpClient httpclient) {
        System.out.println("接口url: " + url);
        HttpPost http = new HttpPost(url);
        CloseableHttpResponse response = null;
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        Iterator iterator = null;
        multipartEntityBuilder.setCharset(Charset.forName(HTTP.UTF_8));//设置请求的编码格式
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);   //这行代码不加的话上传的文件名字如果包含中文，传到服务器后端会显示乱码
        if (null != jsonParam) {
            iterator = jsonParam.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = jsonParam.getString(key);
                multipartEntityBuilder.addPart(key, new StringBody(value, ContentType.create("text/plain", Consts.UTF_8)));
            }
        }
        if (null != filesObject) {
            iterator = filesObject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = filesObject.getString(key);
                String localFilePath = System.getProperty("user.dir") + "/src/main/resources/files/" + value;
                System.err.println("localFilePath: " + localFilePath);
                // 把文件转换成流对象FileBody
//                FileBody bin = new FileBody(new File(localFilePath),ContentType.APPLICATION_OCTET_STREAM);
                FileBody bin = new FileBody(new File(localFilePath), ContentType.create("application/octet-stream", Consts.UTF_8));
                multipartEntityBuilder.addPart(key, bin);

            }
        }

        HttpEntity reqEntity = multipartEntityBuilder.build();
        http.setEntity(reqEntity);
        try {
            response = httpclient.execute(http);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
