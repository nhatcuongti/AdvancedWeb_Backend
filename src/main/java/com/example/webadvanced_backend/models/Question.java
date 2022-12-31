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
@Data
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "presentation_id")
    private Presentation presentation;

    @Column(name = "question", length = 500)
    private String question;

    @Column(name = "is_answered")
    private Boolean isAnswered;
    @Column(name = "created_time")
    private String createdTime;
    @Column(name = "number_vote")
    private Integer numberVote;

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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(Boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

}