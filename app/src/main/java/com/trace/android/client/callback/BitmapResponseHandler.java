package com.trace.android.client.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public abstract class BitmapResponseHandler extends AbsResponseHandler<Bitmap> {

    @Override
    public Bitmap bindData(byte[] data) {
        byte[] buffer = data;
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
    }
}
