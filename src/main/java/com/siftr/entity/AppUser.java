package com.siftr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "appusers")
@NamedQuery(name = "AppUser.findByEmailAddress",
        query = "select u from AppUser u where u.email = ?1")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "email", unique = true)private String email;
    @Column(name = "name")private String name;
    @Column(name = "lastAccess")private Timestamp lastAccess;
    @Column(name = "lastLogin")private Timestamp lastLogin;
    @Column(name = "authenticationToken") private String authenticationToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roles_id")
    @JsonBackReference
    private Role role;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER,
            mappedBy = "appUsers")

    private Set<AppCaseState> appCaseStates = new HashSet<>();


    public AppUser() {

    }

}
