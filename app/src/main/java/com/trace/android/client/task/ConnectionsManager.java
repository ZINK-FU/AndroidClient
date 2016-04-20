package com.trace.android.client.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public class ConnectionsManager {

    private static volatile ConnectionsManager mConnManager;
    private ConnectionsManager(){
        if (mThreadPool == null) {
            mThreadPool = getDefaultThreadPool();
        }
    }
    public static ConnectionsManager getInstance(){
        if (mConnManager == null) {
            synchronized (ConnectionsManager.class){
                if (mConnManager == null) {
                    mConnManager = new ConnectionsManager();
                }
            }
        }
        return mConnManager;
    }

    private ExecutorService mThreadPool;
    public ExecutorService getThreadTool(){
        return mThreadPool;
    }
    private ExecutorService getDefaultThreadPool() {
        return Executors.newCachedThreadPool();
    }
}
