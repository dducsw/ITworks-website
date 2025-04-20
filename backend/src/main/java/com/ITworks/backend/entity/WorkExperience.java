package com.ITworks.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "WorkExperience")
@Getter
@Setter
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WorkID")
    private Integer workId;

    @Column(name = "CandidateID", nullable = false, insertable = false, updatable = false)
    private Integer candidateId;

    @Column(name = "savedCV", nullable = false)
    private String savedCV;

    @Column(name = "YearOfExperience", nullable = false)
    private Integer yearOfExperience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CandidateID", insertable = true, updatable = true)
    private Candidate candidate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "INCLUDE",
        joinColumns = @JoinColumn(name = "WorkID"),
        inverseJoinColumns = @JoinColumn(name = "SkillID")
    )
    private Set<Skill> skills = new HashSet<>();
}