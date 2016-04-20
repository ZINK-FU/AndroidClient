package com.trace.android.client.http;


import com.trace.android.client.domain.FileWrapper;
import com.trace.android.client.domain.HeaderWrapper;
import com.trace.android.client.exception.HttpException;
import com.trace.android.client.listener.UploadProgressListener;

import org.apache.http.protocol.HTTP;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public class HttpURLConnectionTool {

    public static final int CONNECT_TIME_OUT = 5 * 1000;
    public static final int READ_TIME_OUT = 10 * 1000;

    public static HttpURLConnection execute(RequestParams requestParams, UploadProgressListener listener) throws HttpException {
        switch (requestParams.getRequestMethod()) {
            case GET:
                return get(requestParams);
            case POST:
                return post(requestParams, listener);
            default:
                throw new HttpException(HttpException.ExceptionStatus.ParameterException, "the request method" + requestParams.getRequestMethod().name() + "can't be support!");
        }
    }

    private static HttpURLConnection get(RequestParams params) throws HttpException {
        try {
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

            URL url = new URL(params.getUrl() + urlParams);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(params.getRequestMethod().name());
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(CONNECT_TIME_OUT);
            httpURLConnection.setReadTimeout(READ_TIME_OUT);

            List<HeaderWrapper> headerParams = params.getHeader();
            if (headerParams != null && headerParams.size() > 0) {
                for (HeaderWrapper header : headerParams) {
                    httpURLConnection.addRequestProperty(header.getKey(), header.getValue());
                }
            }

            return httpURLConnection;
        }  catch (MalformedURLException e) {
            throw new HttpException(HttpException.ExceptionStatus.ServerException, e.getMessage());
        } catch (IOException e) {
            throw new HttpException(HttpException.ExceptionStatus.IOException, e.getMessage());
        }
    }

    private static HttpURLConnection post(RequestParams params, UploadProgressListener listener) throws HttpException {
        try {
            URL url = new URL(params.getUrl());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(params.getRequestMethod().name());
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(CONNECT_TIME_OUT);
            httpURLConnection.setReadTimeout(READ_TIME_OUT);
            httpURLConnection.setChunkedStreamingMode(1024 * 1024);

            List<HeaderWrapper> headerParams = params.getHeader();
            if (headerParams != null && headerParams.size() > 0) {
                for (HeaderWrapper header : headerParams) {
                    httpURLConnection.addRequestProperty(header.getKey(), header.getValue());
                }
            }

            Map<String, String> contentParams = params.getTextEntities();
            Map<String, FileWrapper> fileParams = params.getFileEntities();
            if ((contentParams != null && contentParams.size() > 0) || (fileParams != null && fileParams.size() > 0)) {
                HttpConnectionMultipartEntity entity = new HttpConnectionMultipartEntity(listener);
                for (Map.Entry<String, String> entry : contentParams.entrySet()) {
                    entity.addPart(entry.getKey(), entry.getValue());
                }

                for (Map.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
                    FileWrapper value = entry.getValue();
                    entity.addPart(entry.getKey(), value.getFile(), null, value.getFileName());
                }

                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + entity.getBoundary());
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(entity.getContentLength()));

                OutputStream connOutStream = new DataOutputStream(httpURLConnection.getOutputStream());
                entity.writeTo(connOutStream);
            }

            return httpURLConnection;
        }  catch (MalformedURLException e) {
            throw new HttpException(HttpException.ExceptionStatus.ServerException, e.getMessage());
        } catch (IOException e) {
            throw new HttpException(HttpException.ExceptionStatus.IOException, e.getMessage());
        } catch (Exception e) {
            throw new HttpException(HttpException.ExceptionStatus.IOException, e.getMessage());
        }
    }
}
