package com.siftr.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name", unique = true)private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
    @JsonManagedReference
    private Set<AppUser> appUsers = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

    public Role() {

    }

    public void add(AppUser users) {
        if (users != null) {
            if (appUsers == null) {
                appUsers = new HashSet<>();
            }

            appUsers.add(users);
            // item.setOrder(this);
        }
    }
}
