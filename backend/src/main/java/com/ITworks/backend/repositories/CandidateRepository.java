package com.ITworks.backend.repository;

import com.ITworks.backend.entity.Candidate;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    @Query("SELECT c FROM Candidate c JOIN c.user u WHERE u.username = :username")
    Optional<Candidate> findByUserUsername(@Param("username") String username);
    
    @Query("SELECT c FROM Candidate c JOIN c.user u WHERE u.email = :email")
    Optional<Candidate> findByUserEmail(@Param("email") String email);
}