package com.ITworks.backend.repository;

import com.ITworks.backend.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Package, String> {
    
}