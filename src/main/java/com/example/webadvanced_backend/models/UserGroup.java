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
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Account user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private GroupInfo group;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_user_in_group", length = 20)
    private RoleUserInGroup roleUserInGroup;
}
