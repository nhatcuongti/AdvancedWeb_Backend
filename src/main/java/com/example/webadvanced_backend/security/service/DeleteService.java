package com.example.webadvanced_backend.security.service;


import com.example.webadvanced_backend.models.Content;
import com.example.webadvanced_backend.models.ContentMultichoice;
import com.example.webadvanced_backend.models.Presentation;
import com.example.webadvanced_backend.models.Slide;
import com.example.webadvanced_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class DeleteService {
    @Autowired
    static PresentationRepository presentationRepository;
    @Autowired
    static AccountRepository accountRepository;
    @Autowired
    static SlideRepository slideRepository;
    @Autowired
    static ContentMultichoiceRepository multichoiceRepository;
    @Autowired
    static ContentRepository contentRepository;

    static void deletePresentation(Presentation presentation){


    }

}
