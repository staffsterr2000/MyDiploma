package com.stasroshchenko.clinic.model.repository.person;

import com.stasroshchenko.clinic.entity.person.DoctorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for working with doctors.
 * @author staffsterr2000
 * @version 1.0
 * @see DoctorData
 */
@Repository
public interface DoctorDataRepository
        extends JpaRepository<DoctorData, Long> {

}
