package com.example.webadvanced_backend.utils;

import org.springframework.beans.factory.annotation.Value;

public class UrlUltils {
    @Value("${root-url}")
    static private String url;

    @Value("${client-url}")
    static private String clientUrl;

    static public String getUrl(){
        return url;
    }
    static public String getClientUrl(){
        return clientUrl;
    }
}
