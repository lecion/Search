package com.yliec;

import java.net.MalformedURLException;

public class Main {

    public static void main(String[] args) {
        try {
            new Gather().getPage("http://www.ibm.com/developerworks/cn/java/j-lo-dyse1/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("错误的url");
        }
    }
}
