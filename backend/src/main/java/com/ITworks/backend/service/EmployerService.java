package com.ITworks.backend.service;

import com.ITworks.backend.entity.Apply;
import com.ITworks.backend.entity.Employer;
import com.ITworks.backend.dto.Job.JobApplicationStatsDTO;
import com.ITworks.backend.dto.Job.JobDTO;
import com.ITworks.backend.dto.Job.JobSearchDTO;
import com.ITworks.backend.dto.Job.JobSearchResponseDTO;

import java.util.Optional;
import java.util.List;

public interface EmployerService {
    List<Employer> findAllEmployers();
    Optional<Employer> findEmployerById(Integer id);

    // Job
    List<JobDTO> getEmployerJobs(Integer employerId);
    
    List<JobDTO> getEmployerJobsByStatus(Integer employerId, String Status);

    /**
     * Lấy danh sách công việc của employer đang đăng nhập
     * @return Danh sách công việc
     */
    List<JobDTO> getCurrentEmployerJobs();
    
    /**
     * Lấy danh sách công việc theo trạng thái của employer đang đăng nhập
     * @param status Trạng thái công việc cần lọc
     * @return Danh sách công việc theo trạng thái
     */
    List<JobDTO> getCurrentEmployerJobsByStatus(String status);

    List<Apply> getJobApplications(Integer employerId, Integer jobId);
    boolean updateApplicationStatus(Integer candidateId, Integer jobId, String newStatus);

    Optional<Employer> findByUsername(String username);
    Optional<Employer> findByEmail(String email);

    // Thêm vào interface EmployerService
    List<JobApplicationStatsDTO> getJobApplicationStats(Integer employerId);
    List<JobApplicationStatsDTO> getCurrentEmployerJobStats();
    
    List<JobSearchResponseDTO> findJobsBySalaryAndDate(Integer employerId, String taxNumber, JobSearchDTO searchDTO);
    List<JobSearchResponseDTO> findCurrentEmployerJobsBySalaryAndDate(JobSearchDTO searchDTO);
}
