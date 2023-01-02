package com.example.webadvanced_backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentParagraphDTO {
    private Content content;
    private String heading;
    private String paragraph;
    private int slideTypeId;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public int getSlideTypeId() {
        return slideTypeId;
    }

    public void setSlideTypeId(int slideTypeId) {
        this.slideTypeId = slideTypeId;
    }
}