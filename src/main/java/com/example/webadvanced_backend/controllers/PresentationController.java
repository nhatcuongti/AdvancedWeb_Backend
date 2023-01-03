package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.repositories.*;
import com.example.webadvanced_backend.requestentities.CreatePresentationRequest;
import com.example.webadvanced_backend.models.*;
import com.example.webadvanced_backend.requestentities.DeletePresentationRequest;
import com.example.webadvanced_backend.requestentities.EditPresentationRequest;
import com.example.webadvanced_backend.requestentities.PresentForGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/presenting/{presentingId}")
    ResponseEntity<?> getPresenting(Principal principal, @PathVariable() int presentingId){
        try {
            PresentationGroup presentationGroup = presentationGroupRepository.findById(presentingId);
            return ResponseEntity.ok(presentationGroup);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }
    @PostMapping(path = "/add")
    public ResponseEntity<?> createAPresentation(@RequestBody CreatePresentationRequest request, Principal principal){
        try {
            Presentation presentation;
            presentation = Presentation.builder().name(request.getPresentationName())
                    .user(accountRepository.findByUsername(principal.getName()))
                    .createdTime( request.getCreatedTime())
                    .build();
            return ResponseEntity.ok(presentationRepository.save(presentation));
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/present-for-group")
    public ResponseEntity<?> presentForGroup(@RequestBody PresentForGroupRequest request, Principal principal){
        try {
            Presentation presentation = presentationRepository.findById(request.getPresentationId());
            PresentationGroup presentationGroup = PresentationGroup.builder()
                    .groupId(request.getGroupId()).isPresenting(true).presentation(presentation).build();
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
            return ResponseEntity.ok(presentationGroupRepository.save(presentationGroup));
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/edit/{id}")
    public ResponseEntity<?> editPresentationName(@RequestBody EditPresentationRequest request, @PathVariable String id) {
        try {
            Optional<Presentation> presentation = presentationRepository.findById(Integer.valueOf(id));
            presentation.get().setName(request.getPresentationName());
            presentation.get().setModifiedTime(request.getEditTime());
            return ResponseEntity.ok(presentationRepository.save(presentation.get()));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/delete")
    public ResponseEntity<?> deletePresentation(@RequestBody DeletePresentationRequest request, Principal principal){
        try {
            Presentation presentation = presentationRepository.findById(request.getPreId());
            // tim list slide
            List<Slide> listSlide = slideRepository.findByPresentation(presentation);
            // tim cac content id cua cac slide
            List<Integer> listContentId = new ArrayList<>();
            for (Slide s: listSlide) {
                   listContentId.add(s.getContent().getId());
            }
            //tim cac list content
            List<Content> listContent = contentRepository.findAllById(listContentId);
            //xoa content multichoice
            List<ContentMultichoice> listMultichoice = new ArrayList<>();
            List<ContentParagraph> listParagraph = new ArrayList<>();
            List<ContentHeading> listHeading = new ArrayList<>();
            for(Content c: listContent ){
                if(c.getSlideType() == 1)
                    listMultichoice.addAll(multichoiceRepository.findByContent(c));
                else if(c.getSlideType() == 2)
                    listParagraph.addAll(paragraphRepository.findByContent(c));
                else if(c.getSlideType() == 3)
                    listHeading.addAll(headingRepository.findByContent(c));
            }
            if(!listMultichoice.isEmpty()){
                //delete votes of each option
                List<Vote> listVote = new ArrayList<>();
                for(ContentMultichoice m : listMultichoice){
                    listVote.addAll(voteRepository.findByOption(m.getOption()));
                }
                voteRepository.deleteAll(listVote);
                multichoiceRepository.deleteAll(listMultichoice);

            }
            if(!listParagraph.isEmpty())
                headingRepository.deleteAll(listHeading);
            if(!listParagraph.isEmpty())
                paragraphRepository.deleteAll(listParagraph);
            // xoa cac slide
            slideRepository.deleteAll(listSlide);
            //xoa presentation_group
            List<PresentationGroup> listPresentationGroup = presentationGroupRepository.findByPresentation(presentation);
            for (PresentationGroup pg: listPresentationGroup) {
                List<Question> listQuestion= questionRepository.findByPresentationGroup(pg);
                //xoa cac question cua tung session
                questionRepository.deleteAll(listQuestion);
            }
            //xoa session
            presentationGroupRepository.deleteAll(listPresentationGroup);

            //

            presentationRepository.delete(presentation);
            return ResponseEntity.ok(presentation);
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }


    @GetMapping(path = "/present1")
    public ResponseEntity<?> present(HttpServletRequest request, Principal principal){
        try {
            HttpSession session = request.getSession();
            session.setAttribute("user", "hello 123");

            return ResponseEntity.ok(session.getId());
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
    @GetMapping(path = "/present2")
    public ResponseEntity<?> present2(HttpServletRequest request){
        try {
            HttpSession session = request.getSession();
            request.getSession().getId();
            return ResponseEntity.ok((String) session.getAttribute("user"));
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
}
