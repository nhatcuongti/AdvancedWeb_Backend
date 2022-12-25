package com.example.webadvanced_backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UrlUltils {
    @Value("${root-url}")
    private String url;

    @Value("${client-url}")
    private String clientUrl;

    public String getUrl(){
        return url;
    }
    public String getClientUrl(){
        return clientUrl;
    }
}
