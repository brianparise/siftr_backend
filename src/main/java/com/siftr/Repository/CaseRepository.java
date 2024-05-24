package com.siftr.Repository;

import com.siftr.entity.BaseCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseRepository extends JpaRepository<BaseCase, Integer> {


}
