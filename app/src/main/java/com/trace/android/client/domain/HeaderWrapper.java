package com.trace.android.client.domain;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public class HeaderWrapper {

    private String key;
    private String value;

    public HeaderWrapper() {
    }

    public HeaderWrapper(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
