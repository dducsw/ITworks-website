package com.ITworks.backend.service;


import com.ITworks.backend.entity.Apply;
import com.ITworks.backend.entity.Employer;
import com.ITworks.backend.entity.Job;
import java.util.Optional;
import java.util.List;

public interface EmployerService {
    List<Employer> findAllEmployers();
    Optional<Employer> findEmployerById(Integer id);

    // Job
    List<Job> getEmployerJobs(Integer employerId);
    List<Job> getEmployerJobsByStatus(Integer employerId, String Status);

    List<Apply> getJobApplications(Integer employerId, Integer jobId);
    boolean updateApplicationStatus(Integer candidateId, Integer jobId, String newStatus);

    Optional<Employer> findByUsername(String username);
    Optional<Employer> findByEmail(String email);

}
