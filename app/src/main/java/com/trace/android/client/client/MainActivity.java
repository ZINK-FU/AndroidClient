package com.trace.android.client.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.trace.android.client.DowloadBitmapActivity;
import com.trace.android.client.R;
import com.trace.android.client.UploadFileActivity;
import com.trace.android.client.UploadTextActivity;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private BaseAdapter mBaseAdapter;

    private Context mContext;
    private String[] mTitltes = new String[]{"上传字符串", "上传文件", "下载Bitmap", "多文件下载"};
    private Class[] mClazz = new Class[]{UploadTextActivity.class, UploadFileActivity.class, DowloadBitmapActivity.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mListView = (ListView) findViewById(R.id.list);
        mBaseAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mTitltes);
        mListView.setAdapter(mBaseAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, mClazz[position]);
                startActivity(intent);
            }
        });
    }
}
