package com.stasroshchenko.diploma.model.repository;

import com.stasroshchenko.diploma.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository
        extends JpaRepository<Visit, Long> {

    // сортує список усіх візитів по
    // 1. статусу (спочатку ACTIVE, потмі SENT і так далі)
    // 2. часу зустрічі (спочатку найближчі)
    // 3. id візіту
    @Query("FROM Visit v ORDER BY " +
            "CASE v.status " +
                "WHEN 'ACTIVE' THEN 1 " +
                "WHEN 'SENT' THEN 2 " +
                "WHEN 'OCCURRED' THEN 3 " +
                "WHEN 'NOT_OCCURRED' THEN 4 " +
                "ELSE 5 " +
            "END, " +
            "v.appointsAt, " +
            "v.id")
    List<Visit> findAllOrdered();

}
