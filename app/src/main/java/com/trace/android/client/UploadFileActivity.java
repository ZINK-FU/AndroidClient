package com.trace.android.client;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.trace.android.client.R;
import com.trace.android.client.callback.StringResponseHandler;
import com.trace.android.client.domain.RequestMethod;
import com.trace.android.client.domain.RequestTool;
import com.trace.android.client.exception.HttpException;
import com.trace.android.client.http.RequestParams;
import com.trace.android.client.listener.UploadProgressListener;
import com.trace.android.client.task.HttpRequest;

import java.io.File;

public class UploadFileActivity extends AppCompatActivity {

    private final String path1 = Environment.getExternalStorageDirectory().getPath() + File.separator + "QQ音乐.apk";
    private final String path2 = Environment.getExternalStorageDirectory().getPath() + File.separator + "陆金所.apk";
    private final String path3 = Environment.getExternalStorageDirectory().getPath() + File.separator + "全民彩票.apk";
    private TextView showTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        showTV = (TextView) findViewById(R.id.tv_show);
    }

    public void start(View view){
        String url = "http://192.168.1.107:8080/TestServerSamples/TestFileServlet";
        RequestParams requestParams = new RequestParams(url);

        requestParams.setRequestMethod(RequestMethod.POST);
        requestParams.setRequestTool(RequestTool.client);

        requestParams.addFileEntity("QQ产品", new File(path1), "QQ音乐.apk");
        requestParams.addFileEntity("金融产品", new File(path2), "陆金所.apk");
        requestParams.addFileEntity("彩票产品", new File(path3), "全民彩票.apk");

        HttpRequest request = new HttpRequest(requestParams, new StringResponseHandler() {
            @Override
            public void onFailure(HttpException exception) {
                showTV.setText("failed!" + exception.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                showTV.setText("success!");
            }
        },
                null,
                new UploadProgressListener() {
            @Override
            public void onProgressUpdateInForeground(int cur, int total) {
                Log.i("ZINK", "CUR:" + cur + "/TOL:" + total);
            }
        });
        request.execute();
    }
}
