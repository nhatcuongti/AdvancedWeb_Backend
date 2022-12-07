package com.example.webadvanced_backend.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/addInfoChannel")
    public void getVote( SimpMessageHeaderAccessor headerAccessor){

    }
}