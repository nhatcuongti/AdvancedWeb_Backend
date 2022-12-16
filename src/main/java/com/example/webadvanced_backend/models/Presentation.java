package com.example.webadvanced_backend.models;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "presentation")
public class Presentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 30)
    private String name;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private Account user;

    @Column(name = "modified_time")
    private Instant modifiedTime;

    @Column(name = "created_time")
    private Instant createdTime;


}