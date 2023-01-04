package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.Account;
import com.example.webadvanced_backend.models.Message;
import com.example.webadvanced_backend.repositories.AccountRepository;
import com.example.webadvanced_backend.repositories.MessageRepository;
import com.example.webadvanced_backend.requestentities.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path ="api/v1/chat")
@CrossOrigin
public class ChatController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    AccountRepository accountRepository;

    @PostMapping(path = "/send-message/{preId}")
     public ResponseEntity<?> sendMessage(@PathVariable Integer preId, @RequestBody
            SendMessageRequest request, Principal principal){
        try{
            // 1 get message and user
            Account currentUser = accountRepository.findByUsername(principal.getName());
            Message message = Message.builder().username(principal.getName()).fullName(currentUser.getFullName()).message(request.getMessage()).presentationGroupId(preId).build();
            // 2 save message into temporary memory
            messageRepository.save(message);
            // 3 send to socket
            simpMessagingTemplate.convertAndSend("/topic/chatroom/" + preId, message);
            return ResponseEntity.ok("success to send message");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping(path = "/load-old-message/{preId}")
    public ResponseEntity<?> loadOldMessage(@PathVariable Integer preId, Principal principal){
        try{
            List<Message> list = messageRepository.findAllByPresentationGroupId(preId);
            return ResponseEntity.ok(list);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
