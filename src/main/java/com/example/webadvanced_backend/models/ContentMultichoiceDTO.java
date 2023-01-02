package com.example.webadvanced_backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentMultichoiceDTO {
    private Content content;
    private List<ContentMultichoice> listContentMultipleChoice;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public List<ContentMultichoice> getListContentMultipleChoice() {
        return listContentMultipleChoice;
    }

    public void setListContentMultipleChoice(List<ContentMultichoice> listContentMultipleChoice) {
        this.listContentMultipleChoice = listContentMultipleChoice;
    }
}