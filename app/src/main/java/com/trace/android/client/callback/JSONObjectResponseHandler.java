package com.trace.android.client.callback;

import com.trace.android.client.exception.HttpException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public abstract class JSONObjectResponseHandler extends AbsResponseHandler<JSONObject> {

    @Override
    public JSONObject bindData(byte[] data) throws HttpException {
        try {
            return new JSONObject(new String(data));
        } catch (JSONException e) {
            throw new HttpException(HttpException.ExceptionStatus.ParseJsonException, "解析JSONObject异常!");
        }
    }
}
