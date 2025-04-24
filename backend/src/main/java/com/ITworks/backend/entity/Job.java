package com.ITworks.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "JOB")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
        name = "InsertJob",
        procedureName = "InsertJob",
        parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "JobType", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "ContractType", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "Level", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "Quantity", type = Integer.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "SalaryFrom", type = BigDecimal.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "SalaryTo", type = BigDecimal.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "RequireExpYear", type = Integer.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "Location", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "JD", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "JobName", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "expireDate", type = LocalDateTime.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "EmployerID", type = Integer.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "TaxNumber", type = String.class)
        }
    ),
    @NamedStoredProcedureQuery(
        name = "UpdateJob",
        procedureName = "UpdateJob",
        parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "JobID", type = Integer.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "JobType", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "ContractType", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "Level", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "Quantity", type = Integer.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "SalaryFrom", type = BigDecimal.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "SalaryTo", type = BigDecimal.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "RequireExpYear", type = Integer.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "Location", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "JD", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "JobName", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "expireDate", type = LocalDateTime.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "JobStatus", type = String.class)
        }
    ),
    @NamedStoredProcedureQuery(
        name = "DeleteJob",
        procedureName = "DeleteJob",
        parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "JobID", type = Integer.class)
        }
    )
})
@Getter
@Setter
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JobID")
    private Integer jobId;
    
    @Column(name = "JobType", nullable = false, length = 50)
    private String jobType;
    
    @Column(name = "ContractType", nullable = false, length = 50)
    private String contractType;
    
    @Column(name = "Level", nullable = false, length = 50)
    private String level;
    
    @Column(name = "Quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "SalaryFrom", nullable = false, precision = 18, scale = 2)
    private BigDecimal salaryFrom;
    
    @Column(name = "SalaryTo", nullable = false, precision = 18, scale = 2)
    private BigDecimal salaryTo;
    
    @Column(name = "RequireExpYear")
    private Integer requireExpYear;
    
    @Column(name = "Location", nullable = false, length = 100)
    private String location;
    
    @Column(name = "JD", nullable = false, columnDefinition = "nvarchar(max)")
    private String jobDescription;
    
    @Column(name = "JobName", nullable = false, length = 100)
    private String jobName;
    
    @Column(name = "postDate")
    private LocalDateTime postDate = LocalDateTime.now();
    
    @Column(name = "expireDate", nullable = false)
    private LocalDateTime expireDate;
    
    @Column(name = "JobStatus", nullable = false, length = 50)
    private String jobStatus;
    
    @Column(name = "EmployerID", nullable = false)
    private Integer employerId;
    
    @Column(name = "TaxNumber", nullable = false, length = 13)
    private String taxNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployerID", referencedColumnName = "employerId", insertable = false, updatable = false)
    private Employer employer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TaxNumber", referencedColumnName = "taxNumber", insertable = false, updatable = false)
    private Company company;
    
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Apply> applications = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "[IN]",
        joinColumns = @JoinColumn(name = "JobID"),
        inverseJoinColumns = @JoinColumn(name = "JCName")
    )
    private Set<JobCategory> categories = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "REQUIRE",
        joinColumns = @JoinColumn(name = "JobID"),
        inverseJoinColumns = @JoinColumn(name = "SkillID")
    )
    private Set<Skill> requiredSkills = new HashSet<>();
}