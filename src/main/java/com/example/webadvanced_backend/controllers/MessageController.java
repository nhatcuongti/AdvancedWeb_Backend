package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.requestentities.SendMessageRequest;
import org.eclipse.aether.resolution.ResolutionErrorPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path ="api/v1/message")
@CrossOrigin
public class MessageController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping(path = "/send-message/{preSessionId}")
     public ResponseEntity<?> sendMessage(@PathVariable String preSessionId, @RequestBody
            SendMessageRequest request){
        try{
            // 1 get message and user
            // 2 save message into temporary memory
            // 3 send to socket
            simpMessagingTemplate.convertAndSend("/topic/chatroom/" + preSessionId);
            return ResponseEntity.ok("success to send message");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
