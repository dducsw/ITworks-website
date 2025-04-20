package com.ITworks.backend.service;


import com.ITworks.backend.entity.Apply;
import com.ITworks.backend.entity.Employer;
import java.util.Optional;
import java.util.List;
import com.ITworks.backend.dto.Job.JobDTO; // Import JobDTO if it exists in this package

public interface EmployerService {
    List<Employer> findAllEmployers();
    Optional<Employer> findEmployerById(Integer id);

    // Job
    List<JobDTO> getEmployerJobs(Integer employerId);
    List<JobDTO> getEmployerJobsByStatus(Integer employerId, String Status);

    List<Apply> getJobApplications(Integer employerId, Integer jobId);
    boolean updateApplicationStatus(Integer candidateId, Integer jobId, String newStatus);

    Optional<Employer> findByUsername(String username);
    Optional<Employer> findByEmail(String email);

}
