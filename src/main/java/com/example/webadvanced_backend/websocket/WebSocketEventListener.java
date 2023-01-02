package com.example.webadvanced_backend.websocket;

import com.example.webadvanced_backend.models.ContentMultichoice;
import com.example.webadvanced_backend.models.Message;
import com.example.webadvanced_backend.models.Slide;
import com.example.webadvanced_backend.repositories.ContentMultichoiceRepository;
import com.example.webadvanced_backend.repositories.MessageRepository;
import com.example.webadvanced_backend.repositories.SlideRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;

@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    SlideRepository slideRepository;
    @Autowired
    ContentMultichoiceRepository multichoiceRepository;
    @Autowired
    MessageRepository messageRepository;


    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("User Disconnected ");

    }
    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Object channelObject = headerAccessor.getHeader("simpDestination");
        if(channelObject != null){
            //get topic
            String [] urlSplit = channelObject.toString().split("/");
            if(urlSplit[2].equals("slide")) // [2] = topic name
            {
                String channelName = urlSplit[3]; // [3] slideId
                // find specific slide
                Slide slide = slideRepository.findById(Integer.parseInt(channelName));
                if (slide.getContent().getSlideType() == 1) {
                    List<ContentMultichoice> multichoiceList = multichoiceRepository.findByContent(slide.getContent());
                    this.simpMessagingTemplate.convertAndSend("/topic/slide" + channelName, multichoiceList);
                }
            }
            else if(urlSplit[2].equals("chatroom")){
//                String preId = urlSplit[3];
//                List<Message> list = messageRepository.findAllByPresentationId(Integer.parseInt(preId));
//                this.simpMessagingTemplate.convertAndSend("/topic/chatroom/" + preId, list);
            }
        }
    }


}
