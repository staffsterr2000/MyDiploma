package com.stasroshchenko.diploma.repository;

import com.stasroshchenko.diploma.entity.database.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository
        extends JpaRepository<Visit, Long> {

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

//    List<Visit> findByClientData(ClientData clientData);
//
//    List<Visit> findByDoctorData(DoctorData doctorData);

}
