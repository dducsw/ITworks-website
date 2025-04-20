package com.ITworks.backend.service;

import com.ITworks.backend.dto.Job.JobCreateDTO;
import com.ITworks.backend.dto.Job.JobDTO;
import com.ITworks.backend.entity.Job;
import java.util.List;


public interface JobService {
    List<JobDTO> findAllJobs();
    JobDTO findJobById(Integer id);

    JobDTO createJob(JobCreateDTO jobCreateDTO);
    JobDTO saveJob(Job job);
    JobDTO updateJob(Integer id, Job jobDetails);
    void deleteJob(Integer id);
    
    List<JobDTO> findJobsByEmployerId(Integer employerId);
    List<JobDTO> findJobsByCompany(String taxNumber);
    List<JobDTO> searchJobs(String keyword);
    List<JobDTO> findJobsByCategory(String categoryName);

    
    List<JobDTO> findJobsByStatus(String status);
    List<JobDTO> findJobsByEmployerIdAndStatus(Integer employerId, String status);
}