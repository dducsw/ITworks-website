package com.ITworks.backend.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "WorkExperience")
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workId;

    @Column(nullable = false)
    private Integer candidateId;

    @Column(nullable = false)
    private String savedCV;

    @Column(nullable = false)
    private Integer yearOfExperience;

    @ManyToOne
    @JoinColumn(name = "CandidateID", insertable = false, updatable = false)
    private Candidate candidate;

    

    // Getters and Setters
    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public String getSavedCV() {
        return savedCV;
    }

    public void setSavedCV(String savedCV) {
        this.savedCV = savedCV;
    }

    public Integer getYearOfExperience() {
        return yearOfExperience;
    }

    public void setYearOfExperience(Integer yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    // public Set<Certification> getCertifications() {
    //     return certifications;
    // }

    // public void setCertifications(Set<Certification> certifications) {
    //     this.certifications = certifications;
    // }

    // public Set<JobHistory> getJobHistories() {
    //     return jobHistories;
    // }

    // public void setJobHistories(Set<JobHistory> jobHistories) {
    //     this.jobHistories = jobHistories;
    // }
}