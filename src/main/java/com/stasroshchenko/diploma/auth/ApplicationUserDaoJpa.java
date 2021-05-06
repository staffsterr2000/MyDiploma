package com.stasroshchenko.diploma.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("jpa")
public interface ApplicationUserDaoJpa extends JpaRepository<ApplicationUser, Integer>, ApplicationUserDao {

}
