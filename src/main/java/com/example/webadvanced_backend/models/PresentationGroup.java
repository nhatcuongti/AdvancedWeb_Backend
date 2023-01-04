package com.example.webadvanced_backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jmx.export.annotation.ManagedNotifications;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "presentation_group")
public class PresentationGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "presentation_id")
    private Presentation presentation;

    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "is_presenting")
    private Boolean isPresenting;

    @Column(name = "current_slide_index")
    private Integer currentSlideIndex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Boolean getIsPresenting() {
        return this.isPresenting;
    }

    public void setIsPresenting(Boolean isPresenting) {
        this.isPresenting = isPresenting;
    }

    public Boolean getPresenting() {
        return isPresenting;
    }

    public void setPresenting(Boolean presenting) {
        isPresenting = presenting;
    }

    public Integer getCurrentSlideIndex() {
        return currentSlideIndex;
    }

    public void setCurrentSlideIndex(Integer currentSlideIndex) {
        this.currentSlideIndex = currentSlideIndex;
    }
}