package com.ITworks.backend.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "JOB_CATEGORY")
public class JobCategory {
    
    @Id
    @Column(name = "JCName", length = 100)
    private String name;
    
    @Column
    private String speciality;
    
    @ManyToMany(mappedBy = "categories")
    private Set<Job> jobs = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "RELATED",
        joinColumns = @JoinColumn(name = "JCName1"),
        inverseJoinColumns = @JoinColumn(name = "JCName2")
    )
    private Set<JobCategory> relatedCategories = new HashSet<>();
    
    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }

    public Set<JobCategory> getRelatedCategories() {
        return relatedCategories;
    }

    public void setRelatedCategories(Set<JobCategory> relatedCategories) {
        this.relatedCategories = relatedCategories;
    }
}