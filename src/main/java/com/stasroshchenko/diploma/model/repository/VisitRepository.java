package com.stasroshchenko.diploma.model.repository;

import com.stasroshchenko.diploma.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repo for working with visit instances.
 * @author staffsterr2000
 * @version 1.0
 * @see Visit
 */
@Repository
public interface VisitRepository
        extends JpaRepository<Visit, Long> {

    /**
     * Runs with SQL query and returns visits sorted by their importance.
     * Sorting via visit status, time of visit and visit ID.
     * @return list of visits
     * @since 1.0
     */
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
