package com.example.webadvanced_backend.websocket;

import com.example.webadvanced_backend.models.ContentMultichoice;
import com.example.webadvanced_backend.models.Slide;
import com.example.webadvanced_backend.repositories.ContentMultichoiceRepository;
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



    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("User Disconnected ");

    }
    @EventListener
    public void handleWebSocketDisconnectListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Object channelObject = headerAccessor.getHeader("simpDestination");
        String channelName = null;
        if(channelObject != null){
            //get topic
            channelName = channelObject.toString().substring(7);
            // find specific slide
            Slide slide = slideRepository.findById(Integer.parseInt(channelName));
            if (slide.getContent().getSlideType() == 1) {
                List<ContentMultichoice> multichoiceList = multichoiceRepository.findByContent(slide.getContent());
                this.simpMessagingTemplate.convertAndSend("/topic/" + channelName, multichoiceList);
            }
        }
    }


}
