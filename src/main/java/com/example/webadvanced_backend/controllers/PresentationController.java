package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.repositories.*;
import com.example.webadvanced_backend.requestBodyEntities.CreatePresentationRequest;
import com.example.webadvanced_backend.models.*;
import com.example.webadvanced_backend.requestBodyEntities.DeletePresentationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping(path = "api/v1/presentation")
public class PresentationController {
    @Autowired
    PresentationRepository presentationRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    SlideRepository slideRepository;
    @Autowired
    ContentMultichoiceRepository multichoiceRepository;
    @Autowired
    ContentRepository contentRepository;

    @GetMapping()
    ResponseEntity<?> getPresentationList(Principal principal){
        try {
            Account currentUser = accountRepository.findByUsername(principal.getName());
            List<Presentation> listPresentation = presentationRepository.findByUser(currentUser);
            return ResponseEntity.ok(listPresentation);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }
    @PostMapping(path = "/2")
    public ResponseEntity<?> createAPresentation(@RequestBody CreatePresentationRequest request, Principal principal){
        try {
            Presentation presentation;
            presentation = Presentation.builder().name(request.getPresentationName())
                    .user(accountRepository.findByUsername(principal.getName())).build();
            return ResponseEntity.ok(presentationRepository.save(presentation));
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/3")
    public ResponseEntity<?> deletePresentation(@RequestBody DeletePresentationRequest request, Principal principal){
        try {
            Presentation presentation = presentationRepository.findById(request.getPreId());
            // tim list slide
            List<Slide> listSlide = slideRepository.findByPresentation(presentation);
            // tim cac content id cua cac slide
            List<Integer> listContentId = new ArrayList<>();
            for (Slide s: listSlide) {
                if(s.getContent().getSlideType() == 1)
                    listContentId.add(s.getContent().getId());
            }
            //tim cac list content
            List<Content> listContent = contentRepository.findAllById(listContentId);
            //xoa content multichoice
            List<ContentMultichoice> listMultichoice = new ArrayList<>();
            for(Content c: listContent ){
                listMultichoice.addAll(multichoiceRepository.findByContent(c));
            }
            multichoiceRepository.deleteAll(listMultichoice);
            // xoa cac slide
            slideRepository.deleteAll(listSlide);
            presentationRepository.delete(presentation);
            return ResponseEntity.ok(presentation);
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
}