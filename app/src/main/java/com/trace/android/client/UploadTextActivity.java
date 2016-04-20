package com.trace.android.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.trace.android.client.callback.StringResponseHandler;
import com.trace.android.client.domain.RequestMethod;
import com.trace.android.client.domain.RequestTool;
import com.trace.android.client.exception.HttpException;
import com.trace.android.client.http.RequestParams;
import com.trace.android.client.task.HttpRequest;

public class UploadTextActivity extends AppCompatActivity {

    private TextView showTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_text);

        showTV = (TextView) findViewById(R.id.tv_show);
    }

    public void start(View view){
        String url = "http://192.168.1.107:8080/TestServerSamples/TestKeyValueServlet";
        RequestParams requestParams = new RequestParams(url);

        requestParams.setRequestMethod(RequestMethod.POST);
        requestParams.setRequestTool(RequestTool.connection);

        requestParams.addTextEntity("name", "Android");
        requestParams.addTextEntity("password", "123");

        HttpRequest request = new HttpRequest(requestParams, new StringResponseHandler() {
            @Override
            public void onFailure(HttpException result) {
                showTV.setText("failed:" + result.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                showTV.setText("success:" + result);
            }
        });
        request.execute();
    }
}
