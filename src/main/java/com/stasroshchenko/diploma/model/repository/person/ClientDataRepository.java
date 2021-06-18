package com.stasroshchenko.diploma.model.repository.person;

import com.stasroshchenko.diploma.entity.person.ClientData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDataRepository
        extends JpaRepository<ClientData, Long> {

}
