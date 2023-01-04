package com.example.webadvanced_backend.models;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PresentationGroupDTO {
    private Integer id;
    private Integer presentationId;
    private Integer groupId;
    private Boolean isPresenting;
    private Integer currentSlideIndex;
}