package com.ITworks.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CANDIDATE")
@Getter
@Setter
public class Candidate {
    
    @Id
    @Column(name = "CandidateID")
    private Integer candidateId;
    
    @OneToOne
    @JoinColumn(name = "CandidateID")
    @MapsId
    private User user;
    
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Apply> applications = new HashSet<>();
    
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkExperience> workExperiences = new HashSet<>();
    
}