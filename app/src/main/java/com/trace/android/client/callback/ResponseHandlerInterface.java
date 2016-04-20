package com.trace.android.client.callback;

import com.trace.android.client.exception.HttpException;
import com.trace.android.client.listener.DownloadProgressListener;

import org.apache.http.HttpResponse;

import java.net.HttpURLConnection;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public interface ResponseHandlerInterface<T> {

    T onPreExecuteInBackground();

    T onPostExecuteInBackground(T result);

    T handle(HttpResponse httpResponse, DownloadProgressListener listener) throws HttpException;

    T handle(HttpResponse httpResponse) throws HttpException;

    T handle(HttpURLConnection httpURLConnection, DownloadProgressListener listener) throws HttpException;

    T handle(HttpURLConnection httpURLConnection) throws HttpException;

    T bindData(byte[] content) throws HttpException;

//    void onFailure(int statusCode, List<HeaderWrapper> headers, HttpException result);
//
//    void onSuccess(int statusCode, List<HeaderWrapper> headers, T result);

    void onFailure(HttpException result);

    void onSuccess(T result);

    boolean isForceCancelled();

    int retryCount();

    void cancel(boolean force);
}
