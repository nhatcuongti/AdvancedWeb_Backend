package com.example.webadvanced_backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Entity
@Table(name = "vote")
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "option_id")
    private COption option;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_vote")
    private Account userVote;

    @Column(name = "created_time")
    private String createdTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public COption getOption() {
        return option;
    }

    public void setOption(COption option) {
        this.option = option;
    }

    public Account getUserVote() {
        return userVote;
    }

    public void setUserVote(Account userVote) {
        this.userVote = userVote;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

}