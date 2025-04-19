package com.ITworks.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "JOB_CATEGORY")
@Getter
@Setter
public class JobCategory {
    
    @Id
    @Column(name = "JCName", length = 100)
    private String name;
    
    @Column(name = "Speciality", length = 100)
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
}