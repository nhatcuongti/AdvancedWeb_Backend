package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.*;
import com.example.webadvanced_backend.repositories.*;
import com.example.webadvanced_backend.requestentities.CreateSlideRequest;
import com.example.webadvanced_backend.requestentities.DeleteSlideRequest;
import com.example.webadvanced_backend.requestentities.EditSlideTitleRequest;
import com.example.webadvanced_backend.requestentities.VoteMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@Controller
@RequestMapping(path ="api/v1/slide")
public class SlideController {
    @Autowired
    PresentationRepository presentationRepository;
    @Autowired
    SlideRepository slideRepository;
    @Autowired
    ContentMultichoiceRepository multichoiceRepository;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    ContentHeadingRepository headingRepository;
    @Autowired
    ContentParagraphRepository paragraphRepository;

    @ResponseBody
    @GetMapping()
    ResponseEntity<?> getSlideList(Principal principal, @RequestParam int preId) {
        try {
            Presentation presentation = presentationRepository.findById(preId);
            if (principal.getName().equals(presentation.getUser().getUsername())) {
                List<Slide> list = slideRepository.findByPresentation(presentation);
                return ResponseEntity.ok(list);
            } else {
                return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @ResponseBody
    @GetMapping("/{slideId}")
    ResponseEntity<?> getSlideDetail(Principal principal, @PathVariable int slideId) {
        try {
            Slide slide = slideRepository.findById(slideId);
            return ResponseEntity.ok(slide);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> createASlide(@RequestBody CreateSlideRequest request, Principal principal) {
        try {
            Presentation presentation = presentationRepository.findById(request.getPreId());
            Content content = Content.builder().slideType(request.getSlideType())
                    .title(request.getTitle()).build();

            Slide slide = Slide.builder().presentation(presentation).content(content).build();

            return ResponseEntity.ok(slideRepository.save(slide));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }

    }

    @PostMapping(path = "/edit/{id}")
    public ResponseEntity<?> editSlideTitle(@RequestBody EditSlideTitleRequest request, @PathVariable String id) {
        try {
            Optional<Slide> optional = slideRepository.findById(Integer.valueOf(id));
            Slide slide = optional.get();


            return ResponseEntity.ok(slideRepository.save(slide));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }

    }

    @PostMapping(path = "/delete")
    public ResponseEntity<?> deleteSlide(@RequestBody DeleteSlideRequest request, Principal principal){
        try {
            // tim list slide
            Slide slide = slideRepository.findById(request.getSlideId());
            //xoa content multichoice
            if(slide.getContent().getSlideType() == 1){
                List<ContentMultichoice> listMultichoice = multichoiceRepository.findByContent(slide.getContent());
                List<Vote> listVote = new ArrayList<>();
                // xoa vote
                for(ContentMultichoice m : listMultichoice){
                    listVote.addAll(voteRepository.findByOption(m.getOption()));
                }
                voteRepository.deleteAll(listVote);
                multichoiceRepository.deleteAll(listMultichoice);
            }
            else if(slide.getContent().getSlideType() == 2){
                List <ContentParagraph> listParagraph = paragraphRepository.findByContent(slide.getContent());
                paragraphRepository.deleteAll(listParagraph);
            }
            else if(slide.getContent().getSlideType() == 3){
                List <ContentHeading> listHeading = headingRepository.findByContent(slide.getContent());
                headingRepository.deleteAll(listHeading);
            }
            // xoa cac slide
            slideRepository.delete(slide);
            return ResponseEntity.ok(slide);
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }


    @PostMapping(path = "/4")
    public ResponseEntity<?> test(@RequestBody VoteMessageRequest request, Principal principal){
        try {

            // xoa cac slide
            List<ContentMultichoice> contentMultichoices = multichoiceRepository.findAll();
            this.simpMessagingTemplate.convertAndSend("/topic/channel11", contentMultichoices);
            return ResponseEntity.ok("cant not delete");

        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
}
