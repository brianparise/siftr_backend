package com.siftr.Repository;

import com.siftr.entity.EpicCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpicCaseRepository extends JpaRepository<EpicCase,Integer> {
}
