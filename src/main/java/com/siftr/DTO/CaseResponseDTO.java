package com.siftr.DTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CaseResponseDTO {

    @Autowired
    private DataSource dataSource;

    public List<CaseData> getCaseResponse(){

        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT t1.id, t1.created_on, t2.patient_name, t2.order_date, t2.order_id,t2.cpt_codes, t2.provider, " +
                    "t2.preferred_facility_name,t2.preferred_surgery_date,t2.special_needs, t3.insurance_name, t3.insurance_group, t3.csn " +
                    "FROM cases t1 " +
                    "LEFT JOIN one_cases t2 ON t1.id = t2.cases_id " +
                    "LEFT JOIN epic_cases t3 ON t1.id = t3.cases_id";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                List<CaseData> resultList = new ArrayList<>();
                while (rs.next()) {
                    CaseData dto = new CaseData();
                    dto.setCaseId(rs.getInt("id"));
                    dto.setCreatedOn(rs.getString("created_on"));
                    dto.setOrderDate(rs.getString("order_date"));
                    dto.setOrderId(rs.getString("order_id"));
                    dto.setPatientName(rs.getString("patient_name"));
                    dto.setInsuranceName(rs.getString("insurance_name"));
                    dto.setInsuranceGroup(rs.getString("insurance_group"));
                    dto.setPreferredFacilityName(rs.getString("preferred_facility_name"));
                    dto.setPreferredSurgeryDate(rs.getString("preferred_surgery_date"));
                    dto.setCptCodes(rs.getString("cpt_codes"));
                    dto.setProviderName(rs.getString("provider"));
                    dto.setSpecialNeeds(rs.getString("special_needs"));
                    dto.setCsn(rs.getString("csn"));
                    resultList.add(dto);
                }
                return resultList;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException ex) {
            // Handle exceptions
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public CaseDataResponse getCaseResponseById(int id) throws Exception{
        CaseData caseData = getCaseById(id);
        CaseDataResponse response = new CaseDataResponse();
        response.setCaseData(caseData);
        response.setError("");
        return response;
    }

    public CaseData getCaseById(Integer id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT t1.id, t1.created_on, t2.patient_name, t2.order_date, t2.cpt_codes, t2.provider, t2.preferred_facility_name, t2.preferred_surgery_date, t2.special_needs, t3.insurance_name, t3.insurance_group, t2.order_id, t3.csn " +
                    "FROM cases t1 " +
                    "LEFT JOIN one_cases t2 ON t1.id = t2.cases_id " +
                    "LEFT JOIN epic_cases t3 ON t1.id = t3.cases_id " + // Added space here
                    "WHERE t1.id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    CaseData dto = new CaseData();
                    dto.setCaseId(rs.getInt("id"));
                    dto.setCreatedOn(rs.getString("created_on"));
                    dto.setPatientName(rs.getString("patient_name"));
                    dto.setOrderDate(rs.getString("order_date"));
                    dto.setCptCodes(rs.getString("cpt_codes"));
                    dto.setProviderName(rs.getString("provider"));
                    dto.setPreferredFacilityName(rs.getString("preferred_facility_name"));
                    dto.setPreferredSurgeryDate(rs.getString("preferred_surgery_date"));
                    dto.setSpecialNeeds(rs.getString("special_needs"));
                    dto.setInsuranceName(rs.getString("insurance_name"));
                    dto.setInsuranceGroup(rs.getString("insurance_group"));
                    dto.setOrderId(rs.getString("order_id"));
                    dto.setCsn(rs.getString("csn"));

                    return dto;
                } else {
                    return null; // Case not found
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public CaseData getCaseByOrderNumber(Integer orderNumber) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT t1.id, t1.created_on, t2.patient_name, t2.order_date, t2.cpt_codes, t2.provider, t2.preferred_facility_name, t2.preferred_surgery_date, t2.special_needs, t3.insurance_name, t3.insurance_group, t2.order_id, t3.csn " +
                    "FROM cases t1 " +
                    "LEFT JOIN one_cases t2 ON t1.id = t2.cases_id " +
                    "LEFT JOIN epic_cases t3 ON t1.id = t3.cases_id " + // Added space here
                    "WHERE t2.order_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, orderNumber);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    CaseData dto = new CaseData();
                    dto.setCaseId(rs.getInt("id"));
                    dto.setCreatedOn(rs.getString("created_on"));
                    dto.setPatientName(rs.getString("patient_name"));
                    dto.setOrderDate(rs.getString("order_date"));
                    dto.setCptCodes(rs.getString("cpt_codes"));
                    dto.setProviderName(rs.getString("provider"));
                    dto.setPreferredFacilityName(rs.getString("preferred_facility_name"));
                    dto.setPreferredSurgeryDate(rs.getString("preferred_surgery_date"));
                    dto.setSpecialNeeds(rs.getString("special_needs"));
                    dto.setInsuranceName(rs.getString("insurance_name"));
                    dto.setInsuranceGroup(rs.getString("insurance_group"));
                    dto.setOrderId(rs.getString("order_id"));
                    dto.setCsn(rs.getString("csn"));

                    return dto;
                } else {
                    return null; // Case not found
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    //TODO check the query for assignment
    public CaseData getAssignmentDetails(Integer id){
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT t1.id, t4.roles_id, t4.name " +
                    "FROM cases t1 " +
                    "LEFT JOIN one_cases t2 ON t1.id = t2.cases_id " +
                    "LEFT JOIN epic_cases t3 ON t1.id = t3.cases_id " +
                    "LEFT JOIN appusers t4 ON t1.id = t4.cases_id " +// Added space here
                    "WHERE t2.order_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    CaseData dto = new CaseData();
                    dto.setCaseId(rs.getInt("id"));
                    dto.setAssignedTo(rs.getString("name"));
                    return dto;
                } else {
                    return null; // Case not found
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
