package com.siftr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "one_cases")
public class OneCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "stored_on", unique = true) private String storedOn;
    @Column(name = "order_id") private String orderId;
    @Column(name = "patient_name") private String patientName;
    @Column(name = "order_date")private String orderDate;
    @Column(name = "cpt_codes") private String cptCodes;
    @Column(name = "provider")private String provider;
    @Column(name = "duration")private String duration;
    @Column(name = "preferred_facility_name")private String preferredFacilityName;
    @Column(name = "preferred_surgery_date")private String preferredSurgeryDate;
    @Column(name = "special_needs") private String specialNeeds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cases_id")
    @JsonBackReference
    private BaseCase baseCase;

    public OneCase(String storedOn, String orderId, String patientName, String orderDate, String cptCodes, String provider, String duration, String preferredFacilityName, String preferredSurgeryDate, String specialNeeds, BaseCase baseCase) {
        this.storedOn = storedOn;
        this.orderId = orderId;
        this.patientName = patientName;
        this.orderDate = orderDate;
        this.cptCodes = cptCodes;
        this.provider = provider;
        this.duration = duration;
        this.preferredFacilityName = preferredFacilityName;
        this.preferredSurgeryDate = preferredSurgeryDate;
        this.specialNeeds = specialNeeds;
        this.baseCase = baseCase;
    }

    public OneCase() {

    }
}
