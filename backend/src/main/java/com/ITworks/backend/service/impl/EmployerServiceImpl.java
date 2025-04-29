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
import com.ITworks.backend.dto.Job.JobSearchDTO;
import com.ITworks.backend.dto.Job.JobSearchResponseDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    @Override
    public List<JobSearchResponseDTO> findJobsBySalaryAndDate(Integer employerId, String taxNumber, JobSearchDTO searchDTO) {
        try {
            // Xử lý tham số mặc định với giá trị hợp lý
            String action = searchDTO.getAction() != null ? String.valueOf(searchDTO.getAction()) : "Get";
            
            if ("search".equalsIgnoreCase(action)) {
                action = "Search";
                List<Object[]> results = jobRepository.findJobsByCategory(action, employerId, taxNumber, searchDTO.getJcName());
                return mapToSalaryDateJobDTOList(results);
            }
            
            BigDecimal salaryFrom = searchDTO.getSalaryFrom() != null ? searchDTO.getSalaryFrom() : BigDecimal.ZERO;
            BigDecimal salaryTo = searchDTO.getSalaryTo() != null ? searchDTO.getSalaryTo() : new BigDecimal("999999999999.99");
            String sortOrder = searchDTO.getSortOrder() != null ? String.valueOf(searchDTO.getSortOrder()) : "DESC";
            Boolean filter;
            if (searchDTO.getFilter() == null) {
                filter = false;
            } else if (searchDTO.getFilter() instanceof Boolean) {
                filter = (Boolean) searchDTO.getFilter();
            } else {
                // Chuyển đổi các kiểu khác sang Boolean
                String filterStr = String.valueOf(searchDTO.getFilter()).toLowerCase();
                filter = "true".equals(filterStr) || "1".equals(filterStr);
            }
            
            // Kiểm tra action hợp lệ
            if (!action.equalsIgnoreCase("Get") && !action.equalsIgnoreCase("Search")) {
                throw new IllegalArgumentException("Action không hợp lệ. Chỉ chấp nhận 'Get' hoặc 'Search'");
            }

            // Kiểm tra sortOrder hợp lệ
            if (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC")) {
                throw new IllegalArgumentException("SortOrder không hợp lệ. Chỉ chấp nhận 'ASC' hoặc 'DESC'");
            }

            // Gọi thủ tục SQL
            List<Object[]> results = jobRepository.findJobsBySalaryAndDate(
                    action,
                    employerId,
                    salaryFrom,
                    salaryTo,
                    taxNumber,
                    searchDTO.getPostDate(),
                    searchDTO.getJcName(),
                    sortOrder,
                    filter
            );

            // Chuyển đổi kết quả thành DTO
            return mapToSalaryDateJobDTOList(results);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gọi thủ tục JobBySalaryAndDate: " + e.getMessage(), e);
        }
    }

    @Override
    public List<JobSearchResponseDTO> findCurrentEmployerJobsBySalaryAndDate(JobSearchDTO searchDTO) {
        // Lấy thông tin employer đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Bạn chưa đăng nhập");
        }
        
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + username));
        
        Employer employer = employerRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Người dùng hiện tại không phải là nhà tuyển dụng"));
        
        
        // Gọi phương thức tìm kiếm với ID của employer đang đăng nhập
        return findJobsBySalaryAndDate(employer.getEmployerId(), employer.getTaxNumber(), searchDTO);
    }
    
    // Helper method to convert SQL results to DTOs
    private List<JobSearchResponseDTO> mapToSalaryDateJobDTOList(List<Object[]> results) {
        List<JobSearchResponseDTO> dtoList = new ArrayList<>();
        
        if (results == null || results.isEmpty()) {
            return dtoList;
        }
        
        for (Object[] row : results) {
            if (row == null || row.length < 7) continue;
            // Debug: log kiểu dữ liệu thực tế
            for (int i = 0; i < row.length; i++) {
                System.out.println("row[" + i + "] = " + row[i] + " (" + (row[i] != null ? row[i].getClass() : "null") + ")");
            }
            JobSearchResponseDTO dto = new JobSearchResponseDTO();
            dto.setJobName(row[0] != null ? (String) row[0] : null);
            dto.setCompanyName(row[1] != null ? (String) row[1] : null);
            dto.setJd(row[2] != null ? (String) row[2] : null);
            
            // Handle salary conversion safely
            if (row[3] != null) {
                if (row[3] instanceof BigDecimal) {
                    dto.setSalaryFrom((BigDecimal) row[3]);
                } else if (row[3] instanceof Number) {
                    dto.setSalaryFrom(new BigDecimal(((Number) row[3]).toString()));
                }
            }
            
            if (row[4] != null) {
                if (row[4] instanceof BigDecimal) {
                    dto.setSalaryTo((BigDecimal) row[4]);
                } else if (row[4] instanceof Number) {
                    dto.setSalaryTo(new BigDecimal(((Number) row[4]).toString()));
                }
            }
            
            // Handle date conversion safely
            if (row[5] != null) {
                if (row[5] instanceof java.sql.Timestamp) {
                    dto.setExpireDate(((java.sql.Timestamp) row[5]).toLocalDateTime());
                } else if (row[5] instanceof java.util.Date) {
                    dto.setExpireDate(new java.sql.Timestamp(((java.util.Date) row[5]).getTime()).toLocalDateTime());
                }
            }
            
            if (row[6] != null) {
                if (row[6] instanceof java.sql.Timestamp) {
                    dto.setPostDate(((java.sql.Timestamp) row[6]).toLocalDateTime());
                } else if (row[6] instanceof java.util.Date) {
                    dto.setPostDate(new java.sql.Timestamp(((java.util.Date) row[6]).getTime()).toLocalDateTime());
                }
            }
            
            if (row.length >= 8) {
                dto.setJobStatus(row[7] != null ? (String) row[7] : null);
            }
            
            dtoList.add(dto);
        }
        
        return dtoList;
    }
}
