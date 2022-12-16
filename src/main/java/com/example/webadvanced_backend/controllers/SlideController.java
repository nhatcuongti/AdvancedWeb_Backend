package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.Content;
import com.example.webadvanced_backend.models.ContentMultichoice;
import com.example.webadvanced_backend.repositories.ContentMultichoiceRepository;
import com.example.webadvanced_backend.requestentities.CreateSlideRequest;
import com.example.webadvanced_backend.models.Presentation;
import com.example.webadvanced_backend.models.Slide;
import com.example.webadvanced_backend.repositories.PresentationRepository;
import com.example.webadvanced_backend.repositories.SlideRepository;
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



    @ResponseBody
    @GetMapping("/{preId}")
    ResponseEntity<?> getSlideList(Principal principal, @PathVariable String preId) {
        try {
            Presentation presentation = presentationRepository.findById(Integer.parseInt(preId));
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
    @GetMapping("/detail/{slideId}")
    ResponseEntity<?> getSlideDetail(Principal principal, @PathVariable String slideId) {
        try {
            Slide slide = slideRepository.findById(Integer.parseInt(slideId));
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

    @PostMapping(path = "/edit/{slideId}")
    public ResponseEntity<?> editSlideTitle(@RequestBody EditSlideTitleRequest request, @PathVariable String slideId) {
        try {
            Optional<Slide> optional = slideRepository.findById(Integer.valueOf(slideId));
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
                multichoiceRepository.deleteAll(listMultichoice);
                slideRepository.delete(slide);
                return ResponseEntity.ok(slide);
            }
            // xoa cac slide
            return ResponseEntity.ok("cant not delete");
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
