package com.ITworks.backend.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SKILL")
public class Skill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer skillId;
    
    @Column(nullable = false, length = 50)
    private String skillName;
    
    @Column(columnDefinition = "nvarchar(max)")
    private String description;
    
    @ManyToMany(mappedBy = "requiredSkills")
    private Set<Job> jobs = new HashSet<>();
    
    // @ManyToMany(mappedBy = "skills")
    // private Set<WorkExperience> workExperiences = new HashSet<>();
    
    // Getters and Setters
    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }

    // public Set<WorkExperience> getWorkExperiences() {
    //     return workExperiences;
    // }

    // public void setWorkExperiences(Set<WorkExperience> workExperiences) {
    //     this.workExperiences = workExperiences;
    // }
}