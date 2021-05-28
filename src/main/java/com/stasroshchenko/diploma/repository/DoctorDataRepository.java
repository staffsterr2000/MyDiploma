package com.stasroshchenko.diploma.repository;

import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorDataRepository
        extends JpaRepository<DoctorData, Long> {

}
