package com.siftr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Configuration
@Component
@Entity
@Getter
@Setter
@Table(name = "access_logs")
@NamedQuery(name = "AccessLog.getAllLogs",
        query = "select l from AccessLog l")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "log")
    private String log;

    @Column(name = "log_time")
    public Timestamp logTime;
}
