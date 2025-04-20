package com.ITworks.backend.repositories;

import com.ITworks.backend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    List<Company> findByTaxNumber(String TaxNumber);
    List<Company> findByIndustry(String industry);
}