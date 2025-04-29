package com.ITworks.backend.repositories;

import com.ITworks.backend.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "SELECT * FROM fn_ThongTinUngTuyenTheoNhaTuyenDung(:employerId)", nativeQuery = true)
    List<Object[]> getJobApplicationStats(@Param("employerId") Integer employerId);

    @Query(value = "EXEC JobBySalaryAndDate " +
            "@Action = :action, " +
            "@EmployerID = :employerId, " +
            "@SalaryFrom = :salaryFrom, " +
            "@SalaryTo = :salaryTo, " +
            "@TaxNumber = :taxNumber, " +
            "@PostDate = :postDate, " +
            "@JCName = :jcName, " +
            "@SortOrder = :sortOrder, " +
            "@Filter = :filter", 
            nativeQuery = true)
    List<Object[]> findJobsBySalaryAndDate(
            @Param("action") String action,
            @Param("employerId") Integer employerId,
            @Param("salaryFrom") BigDecimal salaryFrom,
            @Param("salaryTo") BigDecimal salaryTo,
            @Param("taxNumber") String taxNumber,
            @Param("postDate") LocalDateTime postDate,
            @Param("jcName") String jcName,
            @Param("sortOrder") String sortOrder,
            @Param("filter") Boolean filter 
    );

    // Thêm vào JobRepository một phương thức riêng cho Search với chỉ 2 tham số
    @Query(value = "EXEC JobBySalaryAndDate @Action = :action, @EmployerID = :employerId, @TaxNumber = :taxNumber, @JCName = :jcName", nativeQuery = true)
    List<Object[]> findJobsByCategory(
            @Param("action") String action,
            @Param("employerId") Integer employerId,
            @Param("taxNumber") String taxNumber,
            @Param("jcName") String jcName
        );
}