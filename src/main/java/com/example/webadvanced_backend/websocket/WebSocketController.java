package com.example.webadvanced_backend.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebSocketController {


    @MessageMapping("/public-chat/{preSessionId}")
    public void sendChatMessage(@PathVariable String preSessionId){

    }
}