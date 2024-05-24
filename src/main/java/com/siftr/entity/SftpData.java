package com.siftr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Entity
@Getter
@Setter
@Table(name = "sftpdata")
public class SftpData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "downloadTime")
    public Timestamp downloadTime;
}
