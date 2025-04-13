package com.ITworks.backend.repository;

import com.ITworks.backend.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Integer> {
    List<Employer> findByTaxNumber(String taxNumber);
}