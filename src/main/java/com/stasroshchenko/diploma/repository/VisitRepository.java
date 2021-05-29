package com.stasroshchenko.diploma.repository;

import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository
        extends JpaRepository<Visit, Long> {

    // TODO: query for ordering
//    @Query()
//    List<Visit> findAllOrdered();

    List<Visit> findByClientData(ClientData clientData);

    List<Visit> findByDoctorData(DoctorData doctorData);

}
