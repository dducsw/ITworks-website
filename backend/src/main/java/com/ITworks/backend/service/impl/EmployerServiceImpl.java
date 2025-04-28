package com.ITworks.backend.service.impl;

import com.ITworks.backend.entity.Apply;
import com.ITworks.backend.entity.Employer;
import com.ITworks.backend.entity.Job;
import com.ITworks.backend.mapper.JobMapper;
import com.ITworks.backend.repositories.ApplyRepository;
import com.ITworks.backend.repositories.EmployerRepository;
import com.ITworks.backend.repositories.JobRepository;
import com.ITworks.backend.repositories.UserRepository;
import com.ITworks.backend.service.EmployerService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ITworks.backend.dto.Job.JobDTO; // Import JobDTO
import com.ITworks.backend.dto.Job.JobApplicationStatsDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




// Import User entity
import com.ITworks.backend.entity.User;

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
    
    @Autowired
    private UserRepository userRepository;

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
        return employerRepository.findByUsername(username);
    }

    @Override
    public Optional<Employer> findByEmail(String email) {
        return employerRepository.findByEmail(email);
    }

    @Override
    public List<JobDTO> getCurrentEmployerJobs() {
        // Lấy thông tin người dùng từ Security Context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Not authenticated");
        }
        
        // Lấy ID từ JWT claims
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        // Tìm employer từ user
        Employer employer = employerRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Current user is not an employer"));
        
        // Truy vấn jobs của employer này
        String status = "Đang mở";
        return getEmployerJobsByStatus(employer.getEmployerId(), status);
    }
    
    @Override
    public List<JobDTO> getCurrentEmployerJobsByStatus(String status) {
        // Tương tự như getCurrentEmployerJobs nhưng thêm lọc theo status
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Not authenticated");
        }
        
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        Employer employer = employerRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Current user is not an employer"));
        
        return getEmployerJobsByStatus(employer.getEmployerId(), status);
    }

    @Override
    public List<JobApplicationStatsDTO> getJobApplicationStats(Integer employerId) {
        
        List<Object[]> results = jobRepository.getJobApplicationStats(employerId);
        
        List<JobApplicationStatsDTO> statsList = new java.util.ArrayList<>();
        for (Object[] row : results) {
            JobApplicationStatsDTO stats = new JobApplicationStatsDTO();
            stats.setJobId((Integer) row[0]);
            stats.setJobName((String) row[1]);
            if (row[2] != null) {
                java.sql.Date sqlDate = (java.sql.Date) row[2];
                stats.setCreatedDate(sqlDate.toLocalDate());
            } else {
                stats.setCreatedDate(null);
            }
            stats.setDaNhan((Integer) row[3]);
            stats.setTuChoi((Integer) row[4]);
            stats.setChoDuyet((Integer) row[5]);
            statsList.add(stats);
        }
        
        return statsList;
    }

    @Override
    public List<JobApplicationStatsDTO> getCurrentEmployerJobStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Not authenticated");
        }
        
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        Employer employer = employerRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Current user is not an employer"));
        
        return getJobApplicationStats(employer.getEmployerId());
    }
    }
