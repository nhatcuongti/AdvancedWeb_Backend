package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.PresentationGroup;
import com.example.webadvanced_backend.repositories.PresentationGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@CrossOrigin
@Controller
@RequestMapping(path = "api/v1/notification")
public class PresentingNotificationController {
    @Autowired
    PresentationGroupRepository presentationGroupRepository;

    @GetMapping(path = "/check-presenting/{groupId}")
    public ResponseEntity<?> checkPresenting(@PathVariable int groupId){
        try{
            List<PresentationGroup> listPresenting = presentationGroupRepository.findByGroupId(groupId);
            PresentationGroup currentPresentationSession = null;
            for (PresentationGroup pg: listPresenting) {
                if(pg.getIsPresenting()) {
                    currentPresentationSession = pg;
                    break;
                }
            }
            return ResponseEntity.ok(currentPresentationSession);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
