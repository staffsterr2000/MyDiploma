package com.stasroshchenko.diploma.repository.person;

import com.stasroshchenko.diploma.entity.database.person.PersonData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDataRepository
        extends JpaRepository<PersonData, Long> {

}
