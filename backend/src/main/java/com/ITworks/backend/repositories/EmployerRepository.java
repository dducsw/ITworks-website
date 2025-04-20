package com.ITworks.backend.repositories;


import com.ITworks.backend.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Integer> {
    List<Employer> findByTaxNumber(String taxNumber);
    
    @Query("SELECT e FROM Employer e JOIN e.user u WHERE u.username = :username")
    Optional<Employer> findByUsername(@Param("username") String username);
    
    // Query for email through User relationship
    @Query("SELECT e FROM Employer e JOIN e.user u WHERE u.email = :email")
    Optional<Employer> findByEmail(@Param("email") String email);
}