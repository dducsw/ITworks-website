package com.ITworks.backend.repository;

import com.ITworks.backend.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findByEmployerId(Integer employerId);
    List<Job> findByTaxNumber(String taxNumber);
    List<Job> findByJobName(String jobName);
    List<Job> findByJobStatusAndExpireDateAfter(String status, java.time.LocalDateTime date);
    
    List<Job> findByJobNameContainingIgnoreCase(String keyword);
    
    @Query("SELECT j FROM Job j WHERE j.location LIKE %:location%")
    List<Job> findByLocation(String location);
    
    @Query("SELECT j FROM Job j JOIN j.categories c WHERE c.name = :categoryName")
    List<Job> findByCategory(String categoryName);
    
    @Query("SELECT j FROM Job j JOIN j.requiredSkills s WHERE s.skillName = :skillName")
    List<Job> findByRequiredSkill(String skillName);
}