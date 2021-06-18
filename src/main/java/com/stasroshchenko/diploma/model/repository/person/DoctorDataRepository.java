package com.stasroshchenko.diploma.model.repository.person;

import com.stasroshchenko.diploma.entity.person.DoctorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorDataRepository
        extends JpaRepository<DoctorData, Long> {

}
