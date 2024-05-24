package com.siftr.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CaseDataListResponse {
    private List<CaseData> caseDataList;
    private String error;
}
