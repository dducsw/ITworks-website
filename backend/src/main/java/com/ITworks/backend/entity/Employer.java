package com.ITworks.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EMPLOYER")
public class Employer {
    
    @Id
    private Integer employerId;
    
    @OneToOne
    @JoinColumn(name = "employerId")
    @MapsId
    private User user;
    
    @Column(nullable = false, length = 13)
    @Pattern(regexp = "[0-9]+", message = "Tax number must contain only digits")
    private String taxNumber;
    
    @ManyToOne
    @JoinColumn(name = "taxNumber", referencedColumnName = "taxNumber", insertable = false, updatable = false)
    private Company company;
    
    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Job> jobs = new HashSet<>();
    
    // Getters and Setters
    public Integer getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Integer employerId) {
        this.employerId = employerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }
}