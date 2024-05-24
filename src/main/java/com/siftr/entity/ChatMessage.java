package com.siftr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "chat_messages")
public class ChatMessage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "content")private String content;
    @Column(name = "sent_on")private String sentOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cases_id")
    @JsonBackReference
    private BaseCase baseCase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appusers_id")
    @JsonBackReference
    private AppUser appusers;

    public ChatMessage() {

    }
}
