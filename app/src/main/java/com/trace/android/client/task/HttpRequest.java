package com.trace.android.client.task;

import com.trace.android.client.callback.ResponseHandlerInterface;
import com.trace.android.client.http.RequestParams;
import com.trace.android.client.listener.DownloadProgressListener;
import com.trace.android.client.listener.UploadProgressListener;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public class HttpRequest {

    private RequestParams mRequestParams;

    private ResponseHandlerInterface mReposneHandler;               //请求结果的回调

    public static final int UPLOAD_TAG = 0;
    public static final int DOWNLOAD_TAG = 1;
    private DownloadProgressListener mProgressUpdateListener;         //下载进度更新
    private UploadProgressListener mUploadProgressListener;   //上传进度更新

    private RequestTask mRequestTask;

    public HttpRequest(RequestParams requestParams, ResponseHandlerInterface responseHandler){
        mRequestParams = requestParams;
        mReposneHandler = responseHandler;
    }

    public HttpRequest(RequestParams requestParams, ResponseHandlerInterface responseHandler, DownloadProgressListener listener){
        mRequestParams = requestParams;
        mReposneHandler = responseHandler;
        mProgressUpdateListener = listener;
    }

    public HttpRequest(RequestParams requestParams, ResponseHandlerInterface responseHandler, DownloadProgressListener listener, UploadProgressListener uploadProgressListener){
        mRequestParams = requestParams;
        mReposneHandler = responseHandler;
        mProgressUpdateListener = listener;
        mUploadProgressListener = uploadProgressListener;
    }

    public void execute() {
        mRequestTask = new RequestTask(this);
        mRequestTask.executeOnExecutor(ConnectionsManager.getInstance().getThreadTool());
    }

    public void cancel(boolean force) {
        if (force && mRequestTask != null) {
            mRequestTask.cancel(true);
        }

        if (mReposneHandler != null) {
            mReposneHandler.cancel(force);
        }
    }

    public ResponseHandlerInterface getResponseHandler(){
        return mReposneHandler;
    }

    public DownloadProgressListener getProgressUpdateListener(){
        return mProgressUpdateListener;
    }

    public UploadProgressListener getUploadProgressUpdateListener(){
        return mUploadProgressListener;
    }

    public RequestParams getRequestParams(){
        return mRequestParams;
    }
}
