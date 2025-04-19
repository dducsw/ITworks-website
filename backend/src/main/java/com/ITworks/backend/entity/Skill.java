package com.ITworks.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SKILL")
@Getter
@Setter
public class Skill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SkillID")
    private Integer skillId;
    
    @Column(name = "SkillName", nullable = false, length = 50)
    private String skillName;
    
    @Column(name = "Description", columnDefinition = "nvarchar(max)")
    private String description;
    
    @ManyToMany(mappedBy = "requiredSkills")
    private Set<Job> jobs = new HashSet<>();
    
    @ManyToMany(mappedBy = "skills")
    private Set<WorkExperience> workExperiences = new HashSet<>();
}