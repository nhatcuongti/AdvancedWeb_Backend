package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.Message;
import com.example.webadvanced_backend.requestentities.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping(path ="api/v1/chat")
@CrossOrigin
public class ChatController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping(path = "/send-message/{preSessionId}")
     public ResponseEntity<?> sendMessage(@PathVariable String preSessionId, @RequestBody
            SendMessageRequest request, Principal principal){
        try{
            // 1 get message and user
            Message message = Message.builder().username(principal.getName()).message(request.getMessage()).build();
            // 2 save message into temporary memory
            // 3 send to socket
            simpMessagingTemplate.convertAndSend("/topic/chatroom/" + preSessionId, message);
            return ResponseEntity.ok("success to send message");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
