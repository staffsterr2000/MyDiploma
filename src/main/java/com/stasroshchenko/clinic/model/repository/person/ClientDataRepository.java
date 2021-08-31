package com.stasroshchenko.clinic.model.repository.person;

import com.stasroshchenko.clinic.entity.person.ClientData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for working with clients.
 * @author staffsterr2000
 * @version 1.0
 * @see ClientData
 */
@Repository
public interface ClientDataRepository
        extends JpaRepository<ClientData, Long> {

}
