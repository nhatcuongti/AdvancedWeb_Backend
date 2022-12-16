package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.COption;
import com.example.webadvanced_backend.models.Content;
import com.example.webadvanced_backend.models.ContentMultichoice;
import com.example.webadvanced_backend.repositories.ContentMultichoiceRepository;
import com.example.webadvanced_backend.repositories.ContentRepository;
import com.example.webadvanced_backend.requestentities.CreateMChoiceOptionRequest;
import com.example.webadvanced_backend.requestentities.DeleteSlideTypeRequest;
import com.example.webadvanced_backend.requestentities.EditOptionNameRequest;
import com.example.webadvanced_backend.requestentities.EditSlideTitleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping(path ="api/v1/slide-type")
public class SlideTypeController {

    @Autowired
    ContentMultichoiceRepository multichoiceRepository;
    @Autowired
    ContentRepository contentRepository;

    @ResponseBody
    @GetMapping("/{contentId}")
    ResponseEntity<?> getSlideType(Principal principal, @PathVariable String contentId) {
        try {
            List<ContentMultichoice> list = null;
            Content currentContent = contentRepository.findById(Integer.parseInt(contentId));
            if (currentContent.getSlideType() == 1) {
                list = multichoiceRepository.findByContent(currentContent);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping(path = "/create-multiple-choice")
    public ResponseEntity<?> createAMChoiceOption(@RequestBody CreateMChoiceOptionRequest request, Principal principal) {
        try {
            Content content = contentRepository.findById(request.getContentId());
            ContentMultichoice multichoice = null;
            if (content.getSlideType() == 1) {
                COption option = COption.builder().name(request.getOptionName()).numberVote(0).build();
                multichoice = ContentMultichoice.builder().content(content).option(option).build();
                return ResponseEntity.ok(multichoiceRepository.save(multichoice));
            }
            return ResponseEntity.ok("null");
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/edit")
    public ResponseEntity<?> editTitleSlide(@RequestBody EditSlideTitleRequest request) {
        try {
            Content content = contentRepository.findById(request.getContentId());
            content.setTitle(request.getTitle());
            return ResponseEntity.ok(contentRepository.save(content));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/edit-multiple-choice")
    public ResponseEntity<?> editMultipleChoice(@RequestBody EditOptionNameRequest request, Principal principal){
        try {
            // tim list slide
            if(request.getSlideType() == 1){
                ContentMultichoice multichoice = multichoiceRepository.findById(request.getSlideTypeId());
                COption option = multichoice.getOption();
                option.setName(request.getOptionName());
                return ResponseEntity.ok(multichoiceRepository.save(multichoice));
            }
            return ResponseEntity.ok("cant not delete");
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
    @PostMapping(path = "/delete-multiple-choice")
    public ResponseEntity<?> deleteSlideType(@RequestBody DeleteSlideTypeRequest request, Principal principal){
        try {
            // tim list slide
            if(request.getSlideType() == 1){
               ContentMultichoice multichoice = multichoiceRepository.findById(request.getSlideTypeId());
               multichoiceRepository.delete(multichoice);
               return ResponseEntity.ok(multichoice);
            }
            return ResponseEntity.ok("cant not delete");
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
}
