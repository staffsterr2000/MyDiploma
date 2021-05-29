package com.stasroshchenko.diploma.repository.person;

import com.stasroshchenko.diploma.entity.database.person.ClientData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDataRepository
        extends JpaRepository<ClientData, Long> {

}
