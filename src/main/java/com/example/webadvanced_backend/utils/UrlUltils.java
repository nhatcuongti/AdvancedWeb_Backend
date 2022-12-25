package com.example.webadvanced_backend.utils;

public class UrlUltils {
    static private String url = "https://advancedwebbackend-production-1b23.up.railway.app";
    static private String clientUrl = "https://rei202.github.io/advanced_Web_frontend/#";

//    static private String clientUrl = "http://localhost:3000/advanced_Web_frontend/#";

    //    static private String url = "http://localhost:8080";
    static public String getUrl(){
        return url;
    }
    static public String getClientUrl(){
        return clientUrl;
    }
}
