package com.trace.android.client.task;

import android.os.AsyncTask;

import com.trace.android.client.domain.RequestTool;
import com.trace.android.client.exception.HttpException;
import com.trace.android.client.http.HttpClientTool;
import com.trace.android.client.http.HttpURLConnectionTool;
import com.trace.android.client.listener.DownloadProgressListener;
import com.trace.android.client.listener.UploadProgressListener;

import org.apache.http.HttpResponse;

import java.net.HttpURLConnection;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public class RequestTask extends AsyncTask<Void, Integer, Object> {

    private HttpRequest mHttpRequest;

    public RequestTask(HttpRequest httpRequest){
        mHttpRequest = httpRequest;
    }

    @Override
    protected Object doInBackground(Void... params) {
        int retryCount = 0;
        int retry = 0;
        if (mHttpRequest.getResponseHandler() != null) {
            retryCount = mHttpRequest.getResponseHandler().retryCount();
        }

        return request(retry, retryCount);
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (mHttpRequest.getResponseHandler().isForceCancelled()) {
            return;
        }

        if (mHttpRequest.getResponseHandler() != null) {
            if (result != null && result instanceof Exception) {
                mHttpRequest.getResponseHandler().onFailure((HttpException) result);
            } else {
                mHttpRequest.getResponseHandler().onSuccess(result);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (values[0] == HttpRequest.UPLOAD_TAG) {
            UploadProgressListener listener = mHttpRequest.getUploadProgressUpdateListener();
            if (listener != null) {
                listener.onProgressUpdateInForeground(values[1], values[2]);
            }
        } else if (values[0] == HttpRequest.DOWNLOAD_TAG) {
            DownloadProgressListener listener = mHttpRequest.getProgressUpdateListener();
            if (listener != null) {
                listener.onProgressUpdateInForeground(values[1], values[2]);
            }
        }
    }

    private Object request(int retry, int retryCount) {
        try {
            Object result = null;
            if (mHttpRequest.getResponseHandler() != null) {
                result = mHttpRequest.getResponseHandler().onPreExecuteInBackground();
                if (result != null) {
                    return result;
                }
            }

            //HttpClient
            if (mHttpRequest.getRequestParams().getRequestTool() == RequestTool.client) {
                HttpResponse httpResponse = HttpClientTool.execute(mHttpRequest.getRequestParams(), new UploadProgressListener() {
                    @Override
                    public void onProgressUpdateInForeground(int cur, int total) {
                        publishProgress(HttpRequest.UPLOAD_TAG, cur, total);
                    }
                });

                if (mHttpRequest.getResponseHandler() != null) {
                    if (mHttpRequest.getProgressUpdateListener() != null) {
                        return mHttpRequest.getResponseHandler().handle(httpResponse, new DownloadProgressListener() {

                            @Override
                            public void onProgressUpdateInForeground(int cur, int total) {
                                publishProgress(HttpRequest.DOWNLOAD_TAG, cur, total);
                            }
                        });
                    } else {
                        result = mHttpRequest.getResponseHandler().handle(httpResponse);
                    }
                    return mHttpRequest.getResponseHandler().onPostExecuteInBackground(result);
                } else {
                    return null;
                }
            } else {  //HttpURLConnection
                HttpURLConnection conn = HttpURLConnectionTool.execute(mHttpRequest.getRequestParams(), new UploadProgressListener() {
                    @Override
                    public void onProgressUpdateInForeground(int cur, int total) {
                        publishProgress(HttpRequest.UPLOAD_TAG, cur, total);
                    }
                });

                if (mHttpRequest.getResponseHandler() != null) {
                    if (mHttpRequest.getProgressUpdateListener() != null) {
                        return mHttpRequest.getResponseHandler().handle(conn, new DownloadProgressListener() {
                            @Override
                            public void onProgressUpdateInForeground(int cur, int total) {
                                publishProgress(HttpRequest.DOWNLOAD_TAG, cur, total);
                            }
                        });
                    } else {
                        result = mHttpRequest.getResponseHandler().handle(conn);
                    }
                    return mHttpRequest.getResponseHandler().onPostExecuteInBackground(result);
                } else {
                    return null;
                }
            }
        } catch (HttpException e) {
            if (e.getExceptionStatus() == HttpException.ExceptionStatus.TimeOutException) {
                if (retry < retryCount) {
                    return request(retry++, retryCount);
                }
            }

            return e;
        }
    }
}
