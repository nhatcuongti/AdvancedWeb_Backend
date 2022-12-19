package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.Account;
import com.example.webadvanced_backend.models.ContentMultichoice;
import com.example.webadvanced_backend.models.Slide;
import com.example.webadvanced_backend.models.Vote;
import com.example.webadvanced_backend.repositories.AccountRepository;
import com.example.webadvanced_backend.repositories.ContentMultichoiceRepository;
import com.example.webadvanced_backend.repositories.SlideRepository;
import com.example.webadvanced_backend.repositories.VoteRepository;
import com.example.webadvanced_backend.requestentities.VoteMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(path = "api/v1/vote")
public class VoteController {
    @Autowired
    SlideRepository slideRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    ContentMultichoiceRepository multichoiceRepository;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping(path = "")
    ResponseEntity<?> voteOption(@RequestBody VoteMessageRequest request, Principal principal) {
        try {
            Vote vote = null;
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
                    Account currentUser = accountRepository.findByUsername(principal.getName());
                    vote = Vote.builder().userVote(currentUser)
                            .option(multichoice.getOption())
                            .createdTime(request.getCreatedTime())
                            .build();
                    multichoice.getOption().setNumberVote(multichoice.getOption().getNumberVote() + 1);
                    multichoiceRepository.save(multichoice);
                    voteRepository.save(vote);
                } else {
                    return null;
                }
                List<ContentMultichoice> resultList = multichoiceRepository.findByContent(slide.getContent());
                simpMessagingTemplate.convertAndSend("/topic/" + request.getSlideId(), resultList);
                return ResponseEntity.ok(vote);
            }
            return null;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @GetMapping(path = "/get-vote/{slideTypeId}")
    public ResponseEntity<?> getListVote(@PathVariable int slideTypeId) {
        try {
            ContentMultichoice contentMultichoice = multichoiceRepository.findById(slideTypeId);
            List<Vote> listVote = voteRepository.findByOption(contentMultichoice.getOption());
            return ResponseEntity.ok(listVote);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

}
