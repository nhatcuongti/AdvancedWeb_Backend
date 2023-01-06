package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.*;
import com.example.webadvanced_backend.repositories.*;
import com.example.webadvanced_backend.requestentities.CreatePresentationRequest;
import com.example.webadvanced_backend.requestentities.DeletePresentationRequest;
import com.example.webadvanced_backend.requestentities.EditPresentationRequest;
import com.example.webadvanced_backend.requestentities.PresentForGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@Controller
@RequestMapping(path = "api/v1/presenting")
public class PresentingController {
    @Autowired
    PresentationRepository presentationRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    SlideRepository slideRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ContentMultichoiceRepository multichoiceRepository;
    @Autowired
    ContentHeadingRepository headingRepository;
    @Autowired
    ContentParagraphRepository paragraphRepository;
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    PresentationGroupRepository presentationGroupRepository;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/{presentingId}")
    ResponseEntity<?> getPresenting(Principal principal, @PathVariable() int presentingId){
        try {
            PresentationGroup presentationGroup = presentationGroupRepository.findById(presentingId);
            return ResponseEntity.ok(presentationGroup);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping(path = "/present-for-group")
    public ResponseEntity<?> presentForGroup(@RequestBody PresentForGroupRequest request, Principal principal){
        try {
            Presentation presentation = presentationRepository.findById(request.getPresentationId());
            PresentationGroup presentationGroup = PresentationGroup.builder()
                    .groupId(request.getGroupId()).isPresenting(true).presentation(presentation).currentSlideIndex(0).build();
            simpMessagingTemplate.convertAndSend("/topic/notification/" + request.getGroupId(), true);
            return ResponseEntity.ok(presentationGroupRepository.save(presentationGroup));
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/stop-present-for-group")
    public ResponseEntity<?> stopPresentForGroup(@RequestBody PresentForGroupRequest request, Principal principal){
        try {
            PresentationGroup presentationGroup = presentationGroupRepository.findById(request.getPresentingId());
            presentationGroup.setIsPresenting(false);
            simpMessagingTemplate.convertAndSend("/topic/notification/" + presentationGroup.getGroupId(), false);
            return ResponseEntity.ok(presentationGroupRepository.save(presentationGroup));
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/move-to-slide")
    public ResponseEntity<?> moveToAnotherSlide(@RequestBody PresentationGroupDTO request) {
        try {
            PresentationGroup presentingData = presentationGroupRepository.findById(request.getId()).get();
            presentingData.setCurrentSlideIndex(request.getCurrentSlideIndex());
            simpMessagingTemplate.convertAndSend(String.format("/topic/presenting/%s", presentingData.getId()), request.getCurrentSlideIndex());
            return ResponseEntity.ok(presentationGroupRepository.save(presentingData));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @GetMapping(path = "/presenting-group/{groupId}")
    public ResponseEntity<?> getPresentingGroup(Principal principal, @PathVariable() Integer groupId) {
        try {
            return ResponseEntity.ok(presentationGroupRepository.findPresentationGroupByGroupIdAndAndIsPresenting(groupId, true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
