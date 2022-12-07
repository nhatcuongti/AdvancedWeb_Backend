package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.ContentMultichoice;
import com.example.webadvanced_backend.models.Slide;
import com.example.webadvanced_backend.repositories.ContentMultichoiceRepository;
import com.example.webadvanced_backend.repositories.SlideRepository;
import com.example.webadvanced_backend.requestentities.VoteMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "api/v1/vote")
public class VoteController {
    @Autowired
    SlideRepository slideRepository;
    @Autowired
    ContentMultichoiceRepository multichoiceRepository;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping(path = "/1")
    ResponseEntity<?> voteOption(@RequestBody VoteMessageRequest request) {
        try {
            Slide slide = slideRepository.findById(request.getSlideId());
            if (slide.getContent().getSlideType() == 1) {
                List<ContentMultichoice> multichoiceList = multichoiceRepository.findByContent(slide.getContent());
                ContentMultichoice multichoice = null;
                for (ContentMultichoice c : multichoiceList) {
                    if (c.getOption().getId() == request.getOptionId()) {
                        multichoice = c;
                        break;
                    }
                }
                if (multichoice != null) {
                    multichoice.getOption().setNumberVote(multichoice.getOption().getNumberVote() + 1);
                    multichoiceRepository.save(multichoice);
                } else {
                    return null;
                }
                List<ContentMultichoice> resultList = multichoiceRepository.findByContent(slide.getContent());
                simpMessagingTemplate.convertAndSend("/topic/" + request.getSlideId(), resultList);
                return ResponseEntity.ok(multichoice);
            }
            return null;
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }
}
