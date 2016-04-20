package com.trace.android.client;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.trace.android.client.R;
import com.trace.android.client.callback.BitmapResponseHandler;
import com.trace.android.client.domain.RequestMethod;
import com.trace.android.client.domain.RequestTool;
import com.trace.android.client.exception.HttpException;
import com.trace.android.client.http.RequestParams;
import com.trace.android.client.listener.DownloadProgressListener;
import com.trace.android.client.task.HttpRequest;

public class DowloadBitmapActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dowload_bitmap);

        imageView = (ImageView) findViewById(R.id.iv_show);
    }

    public void start(View view){
        String url = "http://wenwen.soso.com/p/20110131/20110131192226-2124835846.jpg";
        RequestParams requestParams = new RequestParams(url);

        requestParams.setRequestMethod(RequestMethod.GET);
        requestParams.setRequestTool(RequestTool.connection);

        HttpRequest request = new HttpRequest(requestParams, new BitmapResponseHandler() {
            @Override
            public void onFailure(HttpException exception) {
                Log.i("ZINK", "failed:" + exception.getMessage());
            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, new DownloadProgressListener() {
            @Override
            public void onProgressUpdateInForeground(int cur, int total) {
                Log.i("ZINK", "CUR:" + cur + "/TOL:" + total);
            }
        });
        request.execute();
    }
}
