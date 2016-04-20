package com.trace.android.client.callback;

import com.trace.android.client.exception.HttpException;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public abstract class JSONArrayResponseHandler extends AbsResponseHandler<JSONArray> {

    @Override
    public JSONArray bindData(byte[] data) throws HttpException {
        try {
            return new JSONArray(new String(data));
        } catch (JSONException e) {
            throw new HttpException(HttpException.ExceptionStatus.ParseJsonException, "解析JSONArray异常!");
        }
    }
}
