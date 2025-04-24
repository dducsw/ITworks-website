package com.ITworks.backend.repositories;

import com.ITworks.backend.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    
    List<Job> findByJobStatus(String status);
    List<Job> findByEmployerIdAndJobStatus(Integer employerId, String status);
    
    @Procedure(procedureName = "InsertJob")
    Integer callInsertJobProcedure(
        @Param("JobType") String jobType,
        @Param("ContractType") String contractType,
        @Param("Level") String level,
        @Param("Quantity") Integer quantity,
        @Param("SalaryFrom") BigDecimal salaryFrom,
        @Param("SalaryTo") BigDecimal salaryTo,
        @Param("RequireExpYear") Integer requireExpYear,
        @Param("Location") String location,
        @Param("JD") String jobDescription,
        @Param("JobName") String jobName,
        @Param("expireDate") LocalDateTime expireDate,
        @Param("EmployerID") Integer employerId,
        @Param("TaxNumber") String taxNumber
    );
    
    @Procedure(procedureName = "UpdateJob")
    void callUpdateJobProcedure(
        @Param("JobID") Integer jobId,
        @Param("JobType") String jobType,
        @Param("ContractType") String contractType,
        @Param("Level") String level,
        @Param("Quantity") Integer quantity,
        @Param("SalaryFrom") BigDecimal salaryFrom,
        @Param("SalaryTo") BigDecimal salaryTo,
        @Param("RequireExpYear") Integer requireExpYear,
        @Param("Location") String location,
        @Param("JD") String jobDescription,
        @Param("JobName") String jobName,
        @Param("expireDate") LocalDateTime expireDate,
        @Param("JobStatus") String jobStatus
    );
    
    @Procedure(procedureName = "DeleteJob")
    void callDeleteJobProcedure(@Param("JobID") Integer jobId);
}