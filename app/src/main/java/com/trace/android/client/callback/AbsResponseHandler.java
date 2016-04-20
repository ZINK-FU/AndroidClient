package com.trace.android.client.callback;

import android.util.Log;

import com.trace.android.client.exception.HttpException;
import com.trace.android.client.listener.DownloadProgressListener;
import com.trace.android.client.util.TextUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public abstract class AbsResponseHandler<T> implements ResponseHandlerInterface<T>{

    private String mPath;  //文件保存位置

    private boolean mIsCancelled;      //task是否被取消
    private boolean mIsForceCancelled; //task是否被强制取消

    public void checkIfCancelled() throws HttpException {
        if (mIsCancelled){
            throw new HttpException(HttpException.ExceptionStatus.CancelException, "the request has been cancelled!");
        }
    }

    @Override
    public T onPreExecuteInBackground() {
        return null;
    }

    @Override
    public T onPostExecuteInBackground(T result) {
        return result;
    }

    @Override
    public T handle(HttpResponse httpResponse, DownloadProgressListener listener) throws HttpException {
        try {
            checkIfCancelled();
            HttpEntity httpEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            switch (statusCode) {
                case HttpStatus.SC_OK:
                    if (!TextUtil.isBlank(mPath)){
                        FileOutputStream fos = new FileOutputStream(mPath);
                        InputStream is = httpEntity.getContent();
                        writeToFile(is, fos, httpEntity.getContentLength(), listener);
                        return bindData(mPath.getBytes());
                    }else {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        InputStream is = httpEntity.getContent();
                        writeToOutputStream(is, baos, httpEntity.getContentLength(), listener);
                        return bindData(baos.toByteArray());
                    }
                default:
                    throw new HttpException(HttpException.ExceptionStatus.IOException, "statusCode=" + statusCode);
            }
        } catch (FileNotFoundException e) {
            throw new HttpException(HttpException.ExceptionStatus.FileNotFoundException, e.getMessage());
        } catch (IllegalStateException e) {
            throw new HttpException(HttpException.ExceptionStatus.IllegalStateException, e.getMessage());
        } catch (IOException e) {
            throw new HttpException(HttpException.ExceptionStatus.IOException, e.getMessage());
        }
    }

    @Override
    public T handle(HttpResponse httpResponse) throws HttpException {
        return handle(httpResponse, null);
    }

    @Override
    public T handle(HttpURLConnection httpURLConnection, DownloadProgressListener listener) throws HttpException {
        try {
            checkIfCancelled();
            int statusCode = httpURLConnection.getResponseCode();
            switch (statusCode) {
                case HttpURLConnection.HTTP_OK:
                    InputStream is = httpURLConnection.getInputStream();
                    if (!TextUtil.isBlank(mPath)) {
                        FileOutputStream fos = new FileOutputStream(mPath);
                        writeToFile(is, fos, httpURLConnection.getContentLength(), listener);
                        return bindData(mPath.getBytes());
                    } else {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        writeToOutputStream(is, baos, httpURLConnection.getContentLength(), listener);
                        return bindData(baos.toByteArray());
                    }

                default:
                    throw new HttpException(HttpException.ExceptionStatus.IOException, "statusCode=" + statusCode);
            }
        } catch (IOException e) {
            throw new HttpException(HttpException.ExceptionStatus.IOException, e.getMessage());
        }
    }

    @Override
    public T handle(HttpURLConnection httpURLConnection) throws HttpException {
        return handle(httpURLConnection, null);
    }

    @Override
    public T bindData(byte[] content) throws HttpException {
        return (T) content;
    }

    @Override
    public boolean isForceCancelled() {
        return mIsForceCancelled;
    }

    @Override
    public int retryCount() {
        return 0;
    }

    @Override
    public void cancel(boolean force) {
        mIsForceCancelled = force;
        mIsCancelled = true;
    }

    public ResponseHandlerInterface<T> cache(String path){
        mPath = path;
        return this;
    }

    //文件采取断点下载的方式
    private void writeToFile(InputStream is, OutputStream os, long contentLength, DownloadProgressListener requestListener) throws IOException, HttpException {
        byte[] buffer = new byte[1024];
        int len = -1;
        long currentLength = 0;
        while ((len = is.read(buffer)) != -1){
            checkIfCancelled();
            if (requestListener != null){
                currentLength += len;
                requestListener.onProgressUpdateInForeground((int)(currentLength), (int)(contentLength));
            }
            os.write(buffer, 0, len);
        }

        is.close();
        os.close();
    }

    private void writeToOutputStream(InputStream is, OutputStream os, long contentLength, DownloadProgressListener listener) throws IOException, HttpException {
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int len = -1;
        long currentLength = 0;
        while ((len = bis.read(buffer)) != -1){
            checkIfCancelled();
            if (listener != null){
                currentLength += len;
                Log.i("ZINK", "currentLength:" + currentLength + "/contentLength:" + contentLength);
                listener.onProgressUpdateInForeground((int)(currentLength), (int)(contentLength));
            }
            os.write(buffer, 0, len);
        }

        bis.close();
        os.close();
    }

}
