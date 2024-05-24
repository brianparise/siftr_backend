package com.siftr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "epic_cases")
public class EpicCase {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Column(name = "stored_on", unique = true) private String storedOn;
    @Column(name = "csn") private String csn;
    @Column(name = "insurance_name") private String insuranceName;
    @Column(name = "insurance_group") private String insuranceGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cases_id")
    @JsonBackReference
    private BaseCase baseCase;

    public EpicCase(String storedOn, String csn, String insuranceName, String insuranceGroup, BaseCase baseCase) {
        this.storedOn = storedOn;
        this.csn = csn;
        this.insuranceName = insuranceName;
        this.insuranceGroup = insuranceGroup;
        this.baseCase = baseCase;
    }

    public EpicCase() {

    }
}
