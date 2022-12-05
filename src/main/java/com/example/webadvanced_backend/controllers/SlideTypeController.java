package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.COption;
import com.example.webadvanced_backend.models.Content;
import com.example.webadvanced_backend.models.ContentMultichoice;
import com.example.webadvanced_backend.repositories.ContentMultichoiceRepository;
import com.example.webadvanced_backend.repositories.ContentRepository;
import com.example.webadvanced_backend.requestBodyEntities.CreateMChoiceOptionRequest;
import com.example.webadvanced_backend.requestBodyEntities.DeleteSlideTypeRequest;
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
    @GetMapping()
    ResponseEntity<?> getSlideType(Principal principal, @RequestParam int contentId) {
        try {
            List<ContentMultichoice> list = null;
            Content currentContent = contentRepository.findById(contentId);
            if (currentContent.getSlideType() == 1) {
                list = multichoiceRepository.findByContent(currentContent);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping(path = "/2")
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
    @PostMapping(path = "/3")
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
