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
public class Gather {

    public void getPage(String urlStr) throws MalformedURLException {
        URL url = new URL(urlStr);
        try {
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            String content = readContentFromStream(is);
            getUrlFromContent(content);
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
     * @return
     */
    private ArrayList<String> getUrlFromContent(String content) {
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
            System.out.println(rs);
            urls.add(rs);
        }
        return urls;
    }
}
