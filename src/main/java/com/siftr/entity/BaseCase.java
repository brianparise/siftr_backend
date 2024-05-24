package com.siftr.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name="cases")
public class BaseCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "created_on")
    @CreationTimestamp
    private String created_on;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "baseCase")
    @JsonManagedReference
    private Set<OneCase> oneCases = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "baseCase")
    @JsonManagedReference
    private Set<EpicCase> epicCases = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "baseCase")
    @JsonManagedReference
    private Set<AppCaseState> appCaseStates = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "baseCase")
    @JsonManagedReference
    private Set<ChatMessage> chatMessages = new HashSet<>();


    public BaseCase(String created_on) {
        this.created_on = created_on;
    }

    public BaseCase() {

    }
}
