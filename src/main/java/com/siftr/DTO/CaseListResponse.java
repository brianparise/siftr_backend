package com.siftr.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseListResponse {

    private int caseId;
    private String patientName;
    private String orderDate;
    private String caseStatus;
    private String preferredFacilityName;
    private String preferredSurgeryDate;
    private String cptCodes;
    private String duration;
    private String providerName;
    private String specialNeeds;
    private String assignedTo;
    private boolean attentionNeeded;
}
