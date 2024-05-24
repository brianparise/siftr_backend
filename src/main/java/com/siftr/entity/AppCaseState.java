package com.siftr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "appcasestates")
public class AppCaseState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "stored_on")@CreationTimestamp
    private String storedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cases_id")
    @JsonBackReference
    private BaseCase baseCase;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "appcasestates_appusers",
            joinColumns = @JoinColumn(
                    name = "appcasestates_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "appusers_id", referencedColumnName = "id"
            )
    )
    @JsonBackReference
    private Set<AppUser> appUsers = new HashSet<>();
    public AppCaseState() {

    }
}
