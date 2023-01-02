package com.example.webadvanced_backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentHeadingDTO {
    private Content content;
    private String heading;
    private String subheading;
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

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public int getSlideTypeId() {
        return slideTypeId;
    }

    public void setSlideTypeId(int slideTypeId) {
        this.slideTypeId = slideTypeId;
    }
}