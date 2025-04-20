package com.ITworks.backend.service.impl;

import com.ITworks.backend.entity.Apply;
import com.ITworks.backend.entity.Employer;
import com.ITworks.backend.entity.Job;
import com.ITworks.backend.mapper.JobMapper;
import com.ITworks.backend.repositories.ApplyRepository;
import com.ITworks.backend.repositories.EmployerRepository;
import com.ITworks.backend.repositories.JobRepository;
import com.ITworks.backend.service.EmployerService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ITworks.backend.dto.Job.JobDTO; // Import JobDTO
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {


    @Autowired
    private final JobMapper jobMapper;

    @Autowired
    private EmployerRepository employerRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private ApplyRepository applyRepository;
    

    @Override
    public List<Employer> findAllEmployers() {
        return employerRepository.findAll();
    }

    @Override
    public Optional<Employer> findEmployerById(Integer id) {
        return employerRepository.findById(id);
    }

    @Override
    public List<JobDTO> getEmployerJobs(Integer employerId) {
        // Verify employer exists
        if (!employerRepository.existsById(employerId)) {
            throw new IllegalArgumentException("Employer with ID " + employerId + " not found");
        }
        
        return jobMapper.toJobDTOList(jobRepository.findByEmployerId(employerId));
    }

    @Override
    public List<JobDTO> getEmployerJobsByStatus(Integer employerId, String status) {
        // Verify employer exists
        if (!employerRepository.existsById(employerId)) {
            throw new IllegalArgumentException("Employer with ID " + employerId + " not found");
        }
        
        // Get all jobs for this employer, then filter by status
        List<Job> allJobs = jobRepository.findByEmployerId(employerId);
        return jobMapper.toJobDTOList(allJobs.stream()
                .filter(job -> job.getJobStatus().equals(status))
                .collect(Collectors.toList()));
    }

    @Override
    public List<Apply> getJobApplications(Integer employerId, Integer jobId) {
        // Verify that the job exists and belongs to this employer
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job with ID " + jobId + " not found"));
        
        if (!job.getEmployerId().equals(employerId)) {
            throw new IllegalArgumentException("Job with ID " + jobId + " does not belong to employer with ID " + employerId);
        }
        
        // Get applications for this job
        return applyRepository.findByJobId(jobId);
    }

    @Override
    @Transactional
    public boolean updateApplicationStatus(Integer candidateId, Integer jobId, String newStatus) {
        // Find the application
        Apply application = applyRepository.findByCandidateIdAndJobId(candidateId, jobId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
        
        // Update status
        application.setStatus(newStatus);
        applyRepository.save(application);
        
        
        return true;
    }

    @Override
    public Optional<Employer> findByUsername(String username) {
        // The repository method returns Optional<Candidate> which seems incorrect
        // We need to cast or fix the repository method
        return employerRepository.findByUsername(username);
    }

    @Override
    public Optional<Employer> findByEmail(String email) {
        return employerRepository.findByEmail(email);
    }
}
