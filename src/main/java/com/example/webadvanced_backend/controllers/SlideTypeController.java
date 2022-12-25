package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.*;
import com.example.webadvanced_backend.repositories.*;
import com.example.webadvanced_backend.requestentities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping(path = "api/v1/slide-type")
public class SlideTypeController {

    @Autowired
    ContentMultichoiceRepository multichoiceRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    ContentParagraphRepository paragraphRepository;
    @Autowired
    ContentHeadingRepository headingRepository;
    @Autowired
    ContentRepository contentRepository;

    @ResponseBody
    @GetMapping("/{contentId}")
    ResponseEntity<?> getSlideType(Principal principal, @PathVariable int contentId) {
        try {
            Content currentContent = contentRepository.findById(contentId);
            if (currentContent.getSlideType() == 1) {
                List<ContentMultichoice> list = null;
                list = multichoiceRepository.findByContent(currentContent);
                return ResponseEntity.ok(list);
            } else if (currentContent.getSlideType() == 2) {
                List<ContentParagraph> list = null;
                list = paragraphRepository.findByContent(currentContent);
                return ResponseEntity.ok(list);
            } else if (currentContent.getSlideType() == 3) {
                List<ContentHeading> list = null;
                list = headingRepository.findByContent(currentContent);
                return ResponseEntity.ok(list);
            } else {
                return ResponseEntity.ok(new ArrayList<>());
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping(path = "/create-multiple-choice")
    public ResponseEntity<?> createAMChoiceOption(@RequestBody CreateMChoiceOptionRequest request, Principal principal) {
        try {
            Content content = contentRepository.findById(request.getContentId());
            if (content.getSlideType() == 0) {
                content.setSlideType(1);
            }
            COption option = COption.builder().name(request.getOptionName()).numberVote(0).build();
            ContentMultichoice multichoice = ContentMultichoice.builder().content(content).option(option).build();
            return ResponseEntity.ok(multichoiceRepository.save(multichoice));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/create-content-paragraph")
    public ResponseEntity<?> createParagraph(@RequestBody CreateParagraphRequest request, Principal principal) {
        try {
            Content content = contentRepository.findById(request.getContentId());
            if (content.getSlideType() == 0) {
                content.setSlideType(2);
            }
            ContentParagraph paragraph = ContentParagraph.builder()
                    .heading(request.getHeading())
                    .content(content)
                    .paragraph(request.getParagraph())
                    .build();
            return ResponseEntity.ok(paragraphRepository.save(paragraph));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/create-content-heading")
    public ResponseEntity<?> createHeading(@RequestBody CreateHeadingRequest request, Principal principal) {
        try {
            Content content = contentRepository.findById(request.getContentId());
            if (content.getSlideType() == 0) {
                content.setSlideType(3);
            }
            ContentHeading heading = ContentHeading.builder()
                    .heading(request.getHeading())
                    .content(content)
                    .subHeading(request.getSubHeading())
                    .build();
            return ResponseEntity.ok(headingRepository.save(heading));
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
    public ResponseEntity<?> editMultipleChoice(@RequestBody EditOptionNameRequest request, Principal principal) {
        try {
            // tim list slide
            if (request.getSlideType() == 1) {
                ContentMultichoice multichoice = multichoiceRepository.findById(request.getSlideTypeId());
                COption option = multichoice.getOption();
                option.setName(request.getOptionName());
                return ResponseEntity.ok(multichoiceRepository.save(multichoice));
            }
            return ResponseEntity.ok("cant not delete");
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/edit-content-paragraph")
    public ResponseEntity<?> editContentParagraph(@RequestBody EditParagraphRequest request, Principal principal) {
        try {
            // tim list slide
            ContentParagraph paragraph = paragraphRepository.findById(request.getSlideTypeId());
            paragraph.setParagraph(request.getParagraph());
            paragraph.setHeading(request.getHeading());
            return ResponseEntity.ok(paragraphRepository.save(paragraph));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/edit-content-heading")
    public ResponseEntity<?> editContentHeading(@RequestBody EditHeadingRequest request, Principal principal) {
        try {
            // tim list slide
            ContentHeading heading = headingRepository.findById(request.getSlideTypeId());
            heading.setSubHeading(request.getSubHeading());
            heading.setHeading(request.getHeading());
            return ResponseEntity.ok(headingRepository.save(heading));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/delete-multiple-choice")
    public ResponseEntity<?> deleteSlideType(@RequestBody DeleteSlideTypeRequest request, Principal principal) {
        try {
            // tim list slide
            if (request.getSlideType() == 1) {
                ContentMultichoice multichoice = multichoiceRepository.findById(request.getSlideTypeId());
                List<Vote> listVote = voteRepository.findByOption(multichoice.getOption());
                voteRepository.deleteAll(listVote);
                multichoiceRepository.delete(multichoice);
                return ResponseEntity.ok(multichoice);
            }
            return ResponseEntity.ok("cant not delete");
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
}
