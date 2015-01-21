package com.yliec;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 用户管理URL，以及分配URL给Gather
 * Created by Lecion on 1/21/15.
 */
public class URLManager {
    private static URLManager manager = null;

    private CopyOnWriteArraySet<String> visited;

    private BlockingDeque<String> unvisited;

    private String startUrl = "http://www.zhihu.com";

    private URLManager() {
        visited = new CopyOnWriteArraySet<String>();
        unvisited = new LinkedBlockingDeque<String>();
        try {
            unvisited.put(startUrl);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static URLManager getInstance() {
        if (manager == null) {
            synchronized (URLManager.class) {
                if (manager == null) {
                    manager = new URLManager();
                }
            }
        }
        return manager;
    }

    public synchronized String getUrl() {
        //printVisited();
        String url = unvisited.pop();
        visited.add(url);
        return url;
    }

    public synchronized void addUrl(String url) {
        try {
            if (!visited.contains(url)) {
                unvisited.put(url);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printVisited() {
        for (String url : visited) {
            System.out.println("已经访问：" + url);
        }
    }
}
