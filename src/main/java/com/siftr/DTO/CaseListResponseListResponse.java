package com.siftr.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CaseListResponseListResponse {
    private List<CaseListResponse> caseListResponseList;
    private String error;
}
