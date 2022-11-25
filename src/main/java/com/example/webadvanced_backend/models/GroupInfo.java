package com.example.webadvanced_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table()
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "group_name")
    private String groupName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="user_group",
            joinColumns = @JoinColumn(name="group_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private Collection<UserInfo> listUsers;
}
