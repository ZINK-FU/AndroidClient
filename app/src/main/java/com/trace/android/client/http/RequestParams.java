package com.trace.android.client.http;

import com.trace.android.client.domain.FileWrapper;
import com.trace.android.client.domain.HeaderWrapper;
import com.trace.android.client.domain.RequestMethod;
import com.trace.android.client.domain.RequestTool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public class RequestParams {

    private String url;
    private List<HeaderWrapper> headerWrappers;
    private Map<String, String> contentParams;
    private Map<String, FileWrapper> fileWrapperMap;

    private RequestMethod requestMethod;
    private RequestTool requestTool;

    private RequestParams() {
        headerWrappers = new ArrayList<HeaderWrapper>();
        contentParams = new HashMap<String, String>();
        fileWrapperMap = new HashMap<String, FileWrapper>();

        requestMethod = RequestMethod.GET;
        requestTool = RequestTool.client;
    }

    public RequestParams(String url) {
        this();
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<HeaderWrapper> getHeader() {
        return headerWrappers;
    }

    public void addHeader(String key, String value) {
        if (headerWrappers != null) {
            headerWrappers.add(new HeaderWrapper(key, value));
        }
    }

    public Map<String, String> getTextEntities() {
        return contentParams;
    }

    public void addTextEntity(String key, String value) {
        if (contentParams != null) {
            contentParams.put(key, value);
        }
    }

    public Map<String, FileWrapper> getFileEntities() {
        return fileWrapperMap;
    }

    public void addFileEntity(String key, File file) {
       if (fileWrapperMap != null) {
           FileWrapper fileWrapper = new FileWrapper();
           fileWrapper.setFile(file);
           fileWrapperMap.put(key, fileWrapper);
       }
    }

    public void addFileEntity(String key, File file, String fileName) {
        if (fileWrapperMap != null) {
            fileWrapperMap.put(key, new FileWrapper(fileName, file));
        }
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public RequestTool getRequestTool() {
        return requestTool;
    }

    public void setRequestTool(RequestTool requestTool) {
        this.requestTool = requestTool;
    }
}
