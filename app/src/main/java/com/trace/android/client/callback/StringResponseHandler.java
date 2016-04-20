package com.trace.android.client.callback;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public abstract class StringResponseHandler extends AbsResponseHandler<String> {

    @Override
    public String bindData(byte[] data) {
        return new String(data);
    }
}
