package com.ITworks.backend.service.impl;

import com.ITworks.backend.entity.Job;
import com.ITworks.backend.entity.Employer;
import com.ITworks.backend.mapper.JobMapper;
import com.ITworks.backend.repositories.CompanyRepository;
import com.ITworks.backend.repositories.EmployerRepository;
import com.ITworks.backend.repositories.JobRepository;
import com.ITworks.backend.repositories.UserRepository;
import com.ITworks.backend.entity.User;
import com.ITworks.backend.dto.Job.JobCreateDTO;
import com.ITworks.backend.dto.Job.JobDTO;
import com.ITworks.backend.service.JobService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private final JobMapper jobMapper;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private EmployerRepository employerRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public List<JobDTO> findAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobMapper.toJobDTOList(jobs);
    }

    @Override
    public JobDTO findJobById(Integer id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job with ID " + id + " not found"));
        return jobMapper.toJobDTO(job);
    }
    @Override
    public List<JobDTO> findJobsByStatus(String status) {
        List<Job> jobs = jobRepository.findByJobStatus(status);
        return jobMapper.toJobDTOList(jobs);
    }

    @Override
    public List<JobDTO> findJobsByEmployerIdAndStatus(Integer employerId, String status) {
        List<Job> jobs = jobRepository.findByEmployerIdAndJobStatus(employerId, status);
        return jobMapper.toJobDTOList(jobs);
    }

    @Override
    @Transactional
    public JobDTO saveJob(Job job) {
        // Validate job data
        validateJobData(job);
        
        // Set current datetime as post date if not set
        if (job.getPostDate() == null) {
            job.setPostDate(LocalDateTime.now());
        }
        
        Job saveJob = jobRepository.save(job);
        return jobMapper.toJobDTO(saveJob);
    }
    
    // Add this new method to call the stored procedure using EntityManager
    private Integer executeInsertJobProcedure(
            String jobType,
            String contractType,
            String level,
            Integer quantity,
            BigDecimal salaryFrom,
            BigDecimal salaryTo,
            Integer requireExpYear,
            String location,
            String jobDescription,
            String jobName,
            LocalDateTime expireDate,
            Integer employerId,
            String taxNumber) {
        
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("InsertJob");
        
        // Register the parameters
        query.registerStoredProcedureParameter("JobType", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("ContractType", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("Level", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("Quantity", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("SalaryFrom", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("SalaryTo", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("RequireExpYear", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("Location", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("JD", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("JobName", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("expireDate", LocalDateTime.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("EmployerID", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("TaxNumber", String.class, ParameterMode.IN);
        
        // Set parameter values
        query.setParameter("JobType", jobType);
        query.setParameter("ContractType", contractType);
        query.setParameter("Level", level);
        query.setParameter("Quantity", quantity);
        query.setParameter("SalaryFrom", salaryFrom);
        query.setParameter("SalaryTo", salaryTo);
        query.setParameter("RequireExpYear", requireExpYear);
        query.setParameter("Location", location);
        query.setParameter("JD", jobDescription);
        query.setParameter("JobName", jobName);
        query.setParameter("expireDate", expireDate);
        query.setParameter("EmployerID", employerId);
        query.setParameter("TaxNumber", taxNumber);
        
        // Execute the stored procedure
        query.execute();
        
        // After calling the procedure, query for the latest job with matching criteria
        Query idQuery = entityManager.createNativeQuery(
            "SELECT TOP 1 JobID FROM JOB " +
            "WHERE JobType = :jobType AND JobName = :jobName AND EmployerID = :employerId " +
            "ORDER BY postDate DESC"
        );
        
        idQuery.setParameter("jobType", jobType);
        idQuery.setParameter("jobName", jobName);
        idQuery.setParameter("employerId", employerId);
        
        Integer newJobId = ((Number) idQuery.getSingleResult()).intValue();
        return newJobId;
    }
    
    @Override
    @Transactional
    public JobDTO createJob(JobCreateDTO jobCreateDTO) {
        // Xác thực người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // Kiểm tra quyền EMPLOYER
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("EMPLOYER"))) {
            throw new AccessDeniedException("Only employers can create jobs");
        }
        
        // Lấy thông tin employer
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        
        Employer employer = employerRepository.findById(user.getId())
                .orElseThrow(() -> new AccessDeniedException("User is not an employer"));
        
        try {
            // Gọi stored procedure using EntityManager
            Integer newJobId = executeInsertJobProcedure(
                jobCreateDTO.getJobType(),
                jobCreateDTO.getContractType(),
                jobCreateDTO.getLevel(),
                jobCreateDTO.getQuantity(),
                jobCreateDTO.getSalaryFrom(),
                jobCreateDTO.getSalaryTo(),
                jobCreateDTO.getRequireExpYear(),
                jobCreateDTO.getLocation(),
                jobCreateDTO.getJobDescription(),
                jobCreateDTO.getJobName(),
                jobCreateDTO.getExpireDate(),
                employer.getEmployerId(),
                employer.getTaxNumber()
            );
            
            // Lấy job mới tạo
            Job newJob = jobRepository.findById(newJobId)
                    .orElseThrow(() -> new IllegalArgumentException("Job not created"));
                    
            return jobMapper.toJobDTO(newJob);
        } catch (Exception e) {
            throw new RuntimeException("Error creating job: " + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional
    public JobDTO updateJob(Integer id, Job jobDetails) {
        // Xác thực employer
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // Kiểm tra quyền EMPLOYER
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("EMPLOYER"))) {
            throw new AccessDeniedException("Only employers can update jobs");
        }
        
        // Lấy thông tin employer hiện tại
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        
        Employer employer = employerRepository.findById(user.getId())
                .orElseThrow(() -> new AccessDeniedException("User is not an employer"));
        
        // Kiểm tra quyền sở hữu job
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job with ID " + id + " not found"));
        
        if (!existingJob.getEmployerId().equals(employer.getEmployerId())) {
            throw new AccessDeniedException("You can only update your own jobs");
        }
        
        try {
            // Gọi stored procedure
            jobRepository.callUpdateJobProcedure(
                id,
                jobDetails.getJobType(),
                jobDetails.getContractType(),
                jobDetails.getLevel(),
                jobDetails.getQuantity(),
                jobDetails.getSalaryFrom(),
                jobDetails.getSalaryTo(),
                jobDetails.getRequireExpYear(),
                jobDetails.getLocation(),
                jobDetails.getJobDescription(),
                jobDetails.getJobName(),
                jobDetails.getExpireDate(),
                jobDetails.getJobStatus()
            );
            
            // Lấy job sau khi cập nhật
            Job updatedJob = jobRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Job not found after update"));
            
            return jobMapper.toJobDTO(updatedJob);
        } catch (Exception e) {
            throw new RuntimeException("Error updating job: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteJob(Integer id) {
        // Xác thực employer
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // Kiểm tra quyền EMPLOYER
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("EMPLOYER"))) {
            throw new AccessDeniedException("Only employers can delete jobs");
        }
        
        // Lấy thông tin employer hiện tại
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        
        Employer employer = employerRepository.findById(user.getId())
                .orElseThrow(() -> new AccessDeniedException("User is not an employer"));
        
        // Kiểm tra quyền sở hữu job
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job with ID " + id + " not found"));
        
        if (!job.getEmployerId().equals(employer.getEmployerId())) {
            throw new AccessDeniedException("You can only delete your own jobs");
        }
        
        try {
            // Gọi stored procedure
            jobRepository.callDeleteJobProcedure(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting job: " + e.getMessage(), e);
        }
    }

    @Override
    public List<JobDTO> findJobsByEmployerId(Integer employerId) {
        return jobMapper.toJobDTOList(jobRepository.findByEmployerId(employerId));
    }

    @Override
    public List<JobDTO> findJobsByCompany(String taxNumber) {
        return jobMapper.toJobDTOList(jobRepository.findByTaxNumber(taxNumber));
    }

    @Override
    public List<JobDTO> searchJobs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        // Using the available method in the repository
        return jobMapper.toJobDTOList(jobRepository.findByJobNameContainingIgnoreCase(keyword));
    }

    @Override
    public List<JobDTO> findJobsByCategory(String categoryName) {
        return jobMapper.toJobDTOList(jobRepository.findByCategory(categoryName));
    }
    
    // Private validation methods
    private void validateJobData(Job job) {
        // Required fields
        if (job.getJobType() == null || job.getJobType().trim().isEmpty()) {
            throw new IllegalArgumentException("Loại công việc không được để trống");
        }
        
        if (job.getContractType() == null || job.getContractType().trim().isEmpty()) {
            throw new IllegalArgumentException("Loại hợp đồng không được để trống");
        }
        
        if (job.getLevel() == null || job.getLevel().trim().isEmpty()) {
            throw new IllegalArgumentException("Cấp bậc không được để trống");
        }
        
        validatePositiveQuantity(job.getQuantity());
        validateNonNegativeSalary(job.getSalaryFrom());
        validateSalaryRange(job.getSalaryFrom(), job.getSalaryTo());
        
        if (job.getRequireExpYear() != null) {
            validateNonNegativeExperience(job.getRequireExpYear());
        }
        
        if (job.getLocation() == null || job.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Địa điểm làm việc không được để trống");
        }
        
        if (job.getJobDescription() == null || job.getJobDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Mô tả công việc không được để trống");
        }
        
        if (job.getJobName() == null || job.getJobName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên công việc không được để trống");
        }
        
        if (job.getExpireDate() == null) {
            throw new IllegalArgumentException("Ngày hết hạn không được để trống");
        }
        
        validateFutureDate(job.getExpireDate(), LocalDateTime.now());
        
        if (job.getJobStatus() == null || job.getJobStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái công việc không được để trống");
        }
        
        // Check employer and company
        Integer employerId = job.getEmployerId();
        if (employerId == null || !employerRepository.existsById(employerId)) {
            throw new IllegalArgumentException("ID nhà tuyển dụng không hợp lệ");
        }
        
        String taxNumber = job.getTaxNumber();
        if (taxNumber == null || !companyRepository.existsById(taxNumber)) {
            throw new IllegalArgumentException("Mã số thuế không hợp lệ");
        }
        
        // Validate tax number format
        if (!Pattern.matches("^(\\d{10}|\\d{13})$", taxNumber)) {
            throw new IllegalArgumentException("Mã số thuế phải có 10 hoặc 13 chữ số");
        }
    }
    
    private void validatePositiveQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Số lượng tuyển dụng phải lớn hơn 0");
        }
    }
    
    private void validateNonNegativeSalary(BigDecimal salary) {
        if (salary == null || salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Mức lương không được âm");
        }
    }
    
    private void validateSalaryRange(BigDecimal from, BigDecimal to) {
        if (from != null && to.compareTo(from) < 0) {
            throw new IllegalArgumentException("Mức lương tối đa phải lớn hơn hoặc bằng mức lương tối thiểu");
        }
    }
    
    private void validateNonNegativeExperience(Integer years) {
        if (years != null && years < 0) {
            throw new IllegalArgumentException("Số năm kinh nghiệm không được âm");
        }
    }
    
    private void validateFutureDate(LocalDateTime date, LocalDateTime referenceDate) {
        if (!date.isAfter(referenceDate)) {
            throw new IllegalArgumentException("Ngày hết hạn phải sau ngày đăng bài");
        }
    }

    /**
     * Kiểm tra tính hợp lệ của dữ liệu khi cập nhật công việc
     */
    private void validateJobUpdate(Job jobDetails, Job existingJob) {
        // Tạo list để lưu các lỗi
        List<String> errors = new ArrayList<>();
        
        // Kiểm tra các trường String không được rỗng nếu được cung cấp
        if (jobDetails.getJobType() != null && jobDetails.getJobType().trim().isEmpty()) {
            errors.add("Loại công việc không được để trống");
        }
        
        if (jobDetails.getContractType() != null && jobDetails.getContractType().trim().isEmpty()) {
            errors.add("Loại hợp đồng không được để trống");
        }
        
        if (jobDetails.getLevel() != null && jobDetails.getLevel().trim().isEmpty()) {
            errors.add("Cấp bậc không được để trống");
        }
        
        if (jobDetails.getLocation() != null && jobDetails.getLocation().trim().isEmpty()) {
            errors.add("Địa điểm làm việc không được để trống");
        }
        
        if (jobDetails.getJobDescription() != null && jobDetails.getJobDescription().trim().isEmpty()) {
            errors.add("Mô tả công việc không được để trống");
        }
        
        if (jobDetails.getJobName() != null && jobDetails.getJobName().trim().isEmpty()) {
            errors.add("Tên công việc không được để trống");
        }
        
        if (jobDetails.getJobStatus() != null && jobDetails.getJobStatus().trim().isEmpty()) {
            errors.add("Trạng thái công việc không được để trống");
        }
        
        // Kiểm tra số lượng phải dương nếu được cung cấp
        if (jobDetails.getQuantity() != null && jobDetails.getQuantity() <= 0) {
            errors.add("Số lượng tuyển dụng phải lớn hơn 0");
        }
        
        // Kiểm tra mức lương không được âm nếu được cung cấp
        if (jobDetails.getSalaryFrom() != null && jobDetails.getSalaryFrom().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Mức lương không được âm");
        }
        
        // Kiểm tra khoảng lương hợp lệ
        BigDecimal fromSalary = jobDetails.getSalaryFrom() != null ? 
                            jobDetails.getSalaryFrom() : existingJob.getSalaryFrom();
        BigDecimal toSalary = jobDetails.getSalaryTo() != null ?
                            jobDetails.getSalaryTo() : existingJob.getSalaryTo();
                            
        if (fromSalary != null && toSalary != null && toSalary.compareTo(fromSalary) < 0) {
            errors.add("Mức lương tối đa phải lớn hơn hoặc bằng mức lương tối thiểu");
        }
        
        // Kiểm tra số năm kinh nghiệm không được âm nếu được cung cấp
        if (jobDetails.getRequireExpYear() != null && jobDetails.getRequireExpYear() < 0) {
            errors.add("Số năm kinh nghiệm không được âm");
        }
        
        // Kiểm tra ngày hết hạn phải trong tương lai nếu được cung cấp
        if (jobDetails.getExpireDate() != null && !jobDetails.getExpireDate().isAfter(existingJob.getPostDate())) {
            errors.add("Ngày hết hạn phải sau ngày đăng bài");
        }
        
        // Kiểm tra trạng thái công việc hợp lệ
        if (jobDetails.getJobStatus() != null) {
            String status = jobDetails.getJobStatus();
            if (!status.equals("Đang mở") && !status.equals("Đã đóng") && !status.equals("Đã hết hạn")) {
                errors.add("Trạng thái công việc không hợp lệ. Phải là một trong các giá trị: Đang mở, Đã đóng, Đã hết hạn");
            }
        }
        
        // Ném ngoại lệ nếu có bất kỳ lỗi nào
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }
    }

    /**
     * Kiểm tra tính hợp lệ của JobCreateDTO
     */
    private void validateJobCreateDTO(JobCreateDTO dto) {
        List<String> errors = new ArrayList<>();
        
        // Kiểm tra các trường bắt buộc
        if (dto.getJobName() == null || dto.getJobName().trim().isEmpty()) {
            errors.add("Tên công việc không được để trống");
        }
        
        if (dto.getJobType() == null || dto.getJobType().trim().isEmpty()) {
            errors.add("Loại công việc không được để trống");
        }
        
        if (dto.getContractType() == null || dto.getContractType().trim().isEmpty()) {
            errors.add("Loại hợp đồng không được để trống");
        }
        
        if (dto.getLevel() == null || dto.getLevel().trim().isEmpty()) {
            errors.add("Cấp bậc không được để trống");
        }
        
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            errors.add("Số lượng tuyển dụng phải lớn hơn 0");
        }
        
        if (dto.getSalaryFrom() == null || dto.getSalaryFrom().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Mức lương tối thiểu không được âm");
        }
        
        if (dto.getSalaryTo() == null) {
            errors.add("Mức lương tối đa không được để trống");
        } else if (dto.getSalaryFrom() != null && dto.getSalaryTo().compareTo(dto.getSalaryFrom()) < 0) {
            errors.add("Mức lương tối đa phải lớn hơn hoặc bằng mức lương tối thiểu");
        }
        
        if (dto.getRequireExpYear() != null && dto.getRequireExpYear() < 0) {
            errors.add("Số năm kinh nghiệm không được âm");
        }
        
        if (dto.getLocation() == null || dto.getLocation().trim().isEmpty()) {
            errors.add("Địa điểm làm việc không được để trống");
        }
        
        if (dto.getJobDescription() == null || dto.getJobDescription().trim().isEmpty()) {
            errors.add("Mô tả công việc không được để trống");
        }
        
        if (dto.getExpireDate() == null) {
            errors.add("Ngày hết hạn không được để trống");
        } else if (!dto.getExpireDate().isAfter(LocalDateTime.now())) {
            errors.add("Ngày hết hạn phải sau thời điểm hiện tại");
        }
        
        if (dto.getJobStatus() == null || dto.getJobStatus().trim().isEmpty()) {
            errors.add("Trạng thái công việc không được để trống");
        } else {
            String status = dto.getJobStatus();
            if (!status.equals("Đang mở") && !status.equals("Đã đóng") && !status.equals("Đã hết hạn")) {
                errors.add("Trạng thái công việc không hợp lệ. Phải là một trong các giá trị: Đang mở, Đã đóng, Đã hết hạn");
            }
        }
        
        // Kiểm tra nhà tuyển dụng và công ty
        Integer employerId = dto.getEmployerId();
        if (employerId == null || !employerRepository.existsById(employerId)) {
            errors.add("ID nhà tuyển dụng không hợp lệ");
        }
        
        // Ném ngoại lệ nếu có lỗi
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }
    }
}
