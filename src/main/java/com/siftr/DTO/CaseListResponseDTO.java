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
public class CaseListResponseDTO {

    @Autowired
    private DataSource dataSource;

    public List<CaseListResponse> getListOfCases(){
        //assert dataSource != null;

        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT t1.id, t2.patient_name, t2.order_date, t2.cpt_codes, t2.duration, t2.provider, " +
                    "t2.preferred_facility_name,t2.preferred_surgery_date,t2.special_needs " +
                    "FROM cases t1 " +
                    "LEFT JOIN one_cases t2 ON t1.id = t2.cases_id " +
                    "LEFT JOIN epic_cases t3 ON t1.id = t3.cases_id";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                List<CaseListResponse> resultList = new ArrayList<>();
                while (rs.next()) {
                    CaseListResponse dto = new CaseListResponse();
                    dto.setCaseId(rs.getInt("id"));
                    dto.setPatientName(rs.getString("patient_name"));
                    dto.setOrderDate(rs.getString("order_date"));
                    dto.setPreferredFacilityName(rs.getString("preferred_facility_name"));
                    dto.setPreferredSurgeryDate(rs.getString("preferred_surgery_date"));
                    dto.setCptCodes(rs.getString("cpt_codes"));
                    dto.setDuration(rs.getString("duration"));
                    dto.setProviderName(rs.getString("provider"));
                    dto.setSpecialNeeds(rs.getString("special_needs"));
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


}
