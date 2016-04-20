package com.trace.android.client.http;

import com.trace.android.client.domain.FileWrapper;
import com.trace.android.client.domain.HeaderWrapper;
import com.trace.android.client.exception.HttpException;
import com.trace.android.client.listener.UploadProgressListener;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public class HttpClientTool {

    private static HttpClient mHttpClient;

    public static final int TIME_OUT = 1000;
    public static final int CONNECT_TIME_OUT = 5 * 1000;
    public static final int SO_TIME_OUT = 5 * 1000;

    public static HttpResponse execute(RequestParams requestParams, UploadProgressListener listener) throws HttpException {
        switch (requestParams.getRequestMethod()) {
            case GET:
                return get(requestParams);

            case POST:
                return post(requestParams, listener);

            default:
                throw new HttpException(HttpException.ExceptionStatus.ParameterException, "the request method" + requestParams.getRequestMethod().name() + "can't be support!");
        }
    }

    private static HttpResponse get(RequestParams params) throws HttpException {
        try {
            mHttpClient = getDefaultHttpClient();
            Map<String, String> contentParams = params.getTextEntities();
            String urlParams = "";
            if (contentParams != null && contentParams.size() > 0) {
                int i = 0;
                for (Map.Entry<String, String> entry : contentParams.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (i == 0) {
                        urlParams += "?" + key + "=" + URLEncoder.encode(value, HTTP.UTF_8);
                    } else {
                        urlParams += "&" + key + "=" + URLEncoder.encode(value, HTTP.UTF_8);
                    }
                    i++;
                }
            }

            HttpGet httpGet = new HttpGet(params.getUrl() + urlParams);
            List<HeaderWrapper> headerParams = params.getHeader();
            if (headerParams != null && headerParams.size() > 0) {
                for (HeaderWrapper header : headerParams) {
                    httpGet.addHeader(header.getKey(), header.getValue());
                }
            }

            return mHttpClient.execute(httpGet);
        } catch (ConnectTimeoutException e) {
            throw new HttpException(HttpException.ExceptionStatus.TimeOutException, e.getMessage());
        } catch (ClientProtocolException e) {
            throw new HttpException(HttpException.ExceptionStatus.ServerException, e.getMessage());
        } catch (IOException e) {
            throw new HttpException(HttpException.ExceptionStatus.IOException, e.getMessage());
        }
    }

    private static HttpResponse post(RequestParams params, UploadProgressListener listener) throws HttpException {
        mHttpClient = getDefaultHttpClient();
        HttpPost httpPost = new HttpPost(params.getUrl());
        List<HeaderWrapper> headerParams = params.getHeader();
        if (headerParams != null && headerParams.size() > 0) {
            for (HeaderWrapper header : headerParams) {
                httpPost.addHeader(header.getKey(), header.getValue());
            }
        }

        Map<String, String> contentParams = params.getTextEntities();
        Map<String, FileWrapper> fileParams = params.getFileEntities();
        if ((contentParams != null && contentParams.size() > 0) || (fileParams != null && fileParams.size() > 0)) {
            HttpClientMultipartEntity entity = new HttpClientMultipartEntity(listener);
            for (Map.Entry<String, String> entry : contentParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                entity.addPart(key, value);
            }

            for (Map.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
                String key = entry.getKey();
                FileWrapper value = entry.getValue();
                entity.addPart(key, value.getFile(), null, value.getFileName());
            }
            httpPost.setEntity(entity);
        }

        try {
            return mHttpClient.execute(httpPost);
        } catch (ConnectTimeoutException e) {
            throw new HttpException(HttpException.ExceptionStatus.TimeOutException, e.getMessage());
        } catch (ClientProtocolException e) {
            throw new HttpException(HttpException.ExceptionStatus.ServerException, e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(HttpException.ExceptionStatus.UnSupportedEncodingException, e.getMessage());
        } catch (IOException e) {
            throw new HttpException(HttpException.ExceptionStatus.IOException, e.getMessage());
        }
    }

    public static synchronized HttpClient getDefaultHttpClient() {
        if (null == mHttpClient) {
            HttpParams httpParams = new BasicHttpParams();
            HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
            /*
             * 在认证系统或其他可能遭到服务器拒绝应答的情况下（如：登陆失败），
             * 如果发送整个请求体，则会大大降低效率。此时，可以先发送部分请求
             * （如：只发送请求头）进行试探，如果服务器愿意接收，则继续发送请求体
             */
            HttpProtocolParams.setUseExpectContinue(httpParams, true);

            //设置连接管理器的超时
            ConnManagerParams.setTimeout(httpParams, TIME_OUT);

            //设置连接超时
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIME_OUT);

            //设置Socket超时
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIME_OUT);

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

            ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
            return new DefaultHttpClient(connectionManager, httpParams);
        } else {
            return mHttpClient;
        }
    }
}
