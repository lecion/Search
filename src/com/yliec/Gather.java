package com.yliec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网页内容采集器，通过URL，采集页面内容以及相关URL
 * Created by Lecion on 1/21/15.
 */
public class Gather implements Runnable{

    private String[] urlFilter = {"miibeian"};

    private URLManager manager = null;

    public Gather(URLManager manager) {
        this.manager = manager;
    }

    public void getPage(String urlStr) throws MalformedURLException {
        URL url = new URL(urlStr);
        try {
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            String content = readContentFromStream(is);
            getUrlFromContent(content, urlStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readContentFromStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * 从页面内容获取URL列表，正则表达式处理
     * @param content
     * @param urlStr
     * @return
     */
    private ArrayList<String> getUrlFromContent(String content, String urlStr) {
//        System.out.println(content);
        //处理带有query的url，如：http://image.baidu.com/?fr=bk
        int index;
        if ((index = urlStr.lastIndexOf("?")) != -1) {
            urlStr = urlStr.substring(0, index);
        }
        if (!urlStr.endsWith("/")) {
            urlStr += "/";
        }
        //.*?非贪婪模式
        Pattern pattern = Pattern.compile("<a\\s+(href=\".*?\")>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        ArrayList<String> urls = new ArrayList<String>();
        while (matcher.find()) {
//            System.out.println(matcher.group(1));
            String url = matcher.group(1);
            //获取第一对双引号内容，即url
            int start = url.indexOf("\"");
            int end = url.indexOf("\"", start + 1);
            String rs = url.substring(start+1, end);
            //处理相对地址
            if (!(rs.startsWith("http://") || rs.startsWith("https://"))) {
                if (rs.startsWith("#") || rs.startsWith("javascript:") || rs.contains(urlFilter[0])) {
                    continue;
                }
                if (rs.startsWith("//")) {
                    //处理"//"开头的地址
                    rs = urlStr.substring(0, urlStr.indexOf(":")) + rs;
                } else if (rs.startsWith("/")) {
                    rs = urlStr + rs.substring(1);
                } else {
                    rs = urlStr + rs;
                }
                //urls.add(rs);

            }
            manager.addUrl(rs);
            System.out.println(rs);
        }
        return urls;
    }

    @Override
    public void run() {
        while (true) {
            try {
                getPage(manager.getUrl());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }
}
