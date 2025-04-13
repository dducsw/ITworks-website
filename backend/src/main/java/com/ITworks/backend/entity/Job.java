package com.ITworks.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "JOB")
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;
    
    @Column(nullable = false, length = 50)
    private String jobType;
    
    @Column(nullable = false, length = 50)
    private String contractType;
    
    @Column(nullable = false, length = 50)
    private String level;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal salaryFrom;
    
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal salaryTo;
    
    private Integer requireExpYear;
    
    @Column(nullable = false, length = 100)
    private String location;
    
    @Column(nullable = false, name = "JD", columnDefinition = "nvarchar(max)")
    private String jobDescription;
    
    @Column(nullable = false, length = 100)
    private String jobName;
    
    @Column
    private LocalDateTime postDate = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime expireDate;
    
    @Column(nullable = false, length = 50)
    private String jobStatus;
    
    @Column(nullable = false)
    private Integer employerId;
    
    @Column(nullable = false, length = 13)
    private String taxNumber;
    
    @ManyToOne
    @JoinColumn(name = "employerId", referencedColumnName = "employerId", insertable = false, updatable = false)
    private Employer employer;
    
    @ManyToOne
    @JoinColumn(name = "taxNumber", referencedColumnName = "taxNumber", insertable = false, updatable = false)
    private Company company;
    
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private Set<Apply> applications = new HashSet<>();
    
    // @OneToMany(mappedBy = "job")
    // private Set<Notification> notifications = new HashSet<>();
    
    // @OneToMany(mappedBy = "job")
    // private Set<Feedback> feedbacks = new HashSet<>();
    
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
    
    // Getters and Setters
    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(BigDecimal salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    public BigDecimal getSalaryTo() {
        return salaryTo;
    }

    public void setSalaryTo(BigDecimal salaryTo) {
        this.salaryTo = salaryTo;
    }

    public Integer getRequireExpYear() {
        return requireExpYear;
    }

    public void setRequireExpYear(Integer requireExpYear) {
        this.requireExpYear = requireExpYear;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Integer getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Integer employerId) {
        this.employerId = employerId;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Apply> getApplications() {
        return applications;
    }

    public void setApplications(Set<Apply> applications) {
        this.applications = applications;
    }

    // public Set<Notification> getNotifications() {
    //     return notifications;
    // }

    // public void setNotifications(Set<Notification> notifications) {
    //     this.notifications = notifications;
    // }

    // public Set<Feedback> getFeedbacks() {
    //     return feedbacks;
    // }

    // public void setFeedbacks(Set<Feedback> feedbacks) {
    //     this.feedbacks = feedbacks;
    // }

    public Set<JobCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<JobCategory> categories) {
        this.categories = categories;
    }

    public Set<Skill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Set<Skill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
}