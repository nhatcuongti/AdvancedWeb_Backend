package com.example.webadvanced_backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table()
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "group_name")
    private String groupName;

}
