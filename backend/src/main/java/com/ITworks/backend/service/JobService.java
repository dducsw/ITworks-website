package com.ITworks.backend.service;

import com.ITworks.backend.entity.Job;
import java.util.List;
import java.util.Optional;

public interface JobService {
    List<Job> findAllJobs();
    Optional<Job> findJobById(Integer id);
    Job saveJob(Job job);
    Job updateJob(Integer id, Job jobDetails);
    void deleteJob(Integer id);
    List<Job> findJobsByEmployerId(Integer employerId);
    List<Job> findJobsByCompany(Integer TaxNumber);
    List<Job> searchJobs(String keyword);
    List<Job> findActiveJobs();
    List<Job> findJobsByCategory(String categoryName);
}