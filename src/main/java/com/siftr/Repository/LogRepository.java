package com.siftr.Repository;

import com.siftr.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogRepository extends JpaRepository<AccessLog, Integer> {
    @Query("select l from AccessLog l")
    List<AccessLog> getAllLogs();
}