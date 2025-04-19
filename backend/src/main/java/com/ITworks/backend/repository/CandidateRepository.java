package com.ITworks.backend.repository;

import com.ITworks.backend.entity.Candidate;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    Optional<Candidate> findByUsername(String username);
    Optional<Candidate> findByEmail(String email);
}