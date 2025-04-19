package com.ITworks.backend.repository;

import com.ITworks.backend.entity.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, String> {
    // The primary key of JobCategory is a String (name field)
}
