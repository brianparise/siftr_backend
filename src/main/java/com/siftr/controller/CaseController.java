package com.siftr.controller;

import com.siftr.DTO.*;
import com.siftr.authentication.AuthenticationService;
import com.siftr.entity.BaseCase;
import com.siftr.Repository.CaseRepository;
import com.siftr.DTO.*;
import com.siftr.constants.Macros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class CaseController {

    private final CaseRepository caseRepository;

    public CaseController(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    @GetMapping
    public ResponseEntity<List<BaseCase>> getAll(@RequestHeader HttpHeaders headers) {
        String applicationToken = headers.getFirst("ApplicationKey");
        String bearerToken = headers.getFirst("Authorization");
        if(Objects.equals(authenticationService.Authorize(applicationToken, bearerToken), Macros.kApplicationAuthorizationSuccess)) {
            List<BaseCase> allCases = caseRepository.findAll();
            return ResponseEntity.ok(allCases);
        }
        return ResponseEntity.badRequest().build();
    }

    @Autowired
    private CaseResponseDTO dao = new CaseResponseDTO();

    @Autowired
    private CaseListResponseDTO dto = new CaseListResponseDTO();
    @Autowired
    private AuthenticationService authenticationService;
    // Endpoint to return data through API
    @PostMapping("/all_cases")
    public CaseDataListResponse getDataFromTablesAPI(@RequestHeader HttpHeaders headers) {
        CaseDataListResponse response = new CaseDataListResponse();
        try {
            String applicationToken = headers.getFirst("ApplicationKey");
            String bearerToken = headers.getFirst("Authorization");
            if (Objects.equals(authenticationService.Authorize(applicationToken, bearerToken), Macros.kApplicationAuthorizationSuccess)) {

                List<CaseData> caseList = dao.getCaseResponse();
                response.setCaseDataList(caseList);
                response.setError("");
                return response;
            }
            response.setError(Macros.kTokenFailed);
            return response;
        }catch(Exception e) {
            response.setError(Macros.kDatabaseError);
            return response;
        }

    }

    @PostMapping("/list_of_cases")
    public CaseListResponseListResponse getData(@RequestHeader HttpHeaders headers){
        CaseListResponseListResponse response = new CaseListResponseListResponse();
        try {
            String applicationToken = headers.getFirst("ApplicationKey");
            String bearerToken = headers.getFirst("Authorization");
            if(Objects.equals(authenticationService.Authorize(applicationToken, bearerToken), Macros.kApplicationAuthorizationSuccess)) {
                List<CaseListResponse> caseList = dto.getListOfCases();
                response.setCaseListResponseList(caseList);
                response.setError("");
                return response;
            }
            response.setError(Macros.kTokenFailed);
            return response;
        }catch(Exception ex)
        {
            response.setError(Macros.kDatabaseError);
            return response;
        }
    }

    @PostMapping("/all_cases/{id}")
    public CaseDataResponse getById(@PathVariable Integer id, @RequestHeader HttpHeaders headers) {

        CaseDataResponse response = new CaseDataResponse();
        try {
            String applicationToken = headers.getFirst("ApplicationKey");
            String bearerToken = headers.getFirst("Authorization");
            if (Objects.equals(authenticationService.Authorize(applicationToken, bearerToken), Macros.kApplicationAuthorizationSuccess)) {

                CaseData caseData = dao.getCaseById(id);

                if (caseData != null) {
                    response.setCaseData(caseData);
                    response.setError("");
                    return response;
                }
            }
            response.setError(Macros.kTokenFailed);
            return response;
        }catch(Exception ex)
        {
            response.setError(Macros.kDatabaseError);
            return response;
        }
    }

    @PostMapping("/case_by_order_number/{orderNumber}")
    public CaseDataResponse getByOrderNumber(@PathVariable Integer orderNumber, @RequestHeader HttpHeaders headers) {
        CaseDataResponse response = new CaseDataResponse();
        try {
            String applicationToken = headers.getFirst("ApplicationKey");
            String bearerToken = headers.getFirst("Authorization");
            if (Objects.equals(authenticationService.Authorize(applicationToken, bearerToken), Macros.kApplicationAuthorizationSuccess)) {
                CaseData caseData = dao.getCaseByOrderNumber(orderNumber);
                if (caseData != null) {
                    response.setCaseData(caseData);
                    response.setError("");
                    return response;
                }
            }
            response.setError(Macros.kTokenFailed);
            return response;
        }catch(Exception ex)
        {
            response.setError(Macros.kDatabaseError);
            return response;
        }
    }

    @GetMapping("/all_cases/{id}/check-assignment")
    public CaseDataResponse checkAssignment(@PathVariable Integer id, @RequestHeader HttpHeaders headers){
        CaseDataResponse response = new CaseDataResponse();
        try {

            String applicationToken = headers.getFirst("ApplicationKey");
            String bearerToken = headers.getFirst("Authorization");
            if (Objects.equals(authenticationService.Authorize(applicationToken, bearerToken), Macros.kApplicationAuthorizationSuccess)) {
                CaseData caseData = dao.getAssignmentDetails(id);
                if (caseData != null) {
                    response.setCaseData(caseData);
                    response.setError("");
                    return response;
                }
            }
            response.setError(Macros.kTokenFailed);
            return response;
        }catch(Exception ex)
        {
            response.setError(Macros.kDatabaseError);
            return response;
        }
    }
}
