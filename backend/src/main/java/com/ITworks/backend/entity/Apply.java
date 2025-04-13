package com.ITworks.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "APPLY")
public class Apply {
    
    @EmbeddedId
    private ApplyId id;
    
    @ManyToOne
    @MapsId("candidateId")
    @JoinColumn(name = "CandidateID")
    private Candidate candidate;
    
    @ManyToOne
    @MapsId("jobId")
    @JoinColumn(name = "JobID")
    private Job job;
    
    @Column(columnDefinition = "nvarchar(max)")
    private String coverLetter;
    
    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();
    
    @Column(length = 50)
    private String status;
    
    @Column
    private String uploadCV;
    
    // Embedded ID class
    @Embeddable
    public static class ApplyId implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        
        @Column(name = "CandidateID")
        private Integer candidateId;
        
        @Column(name = "JobID")
        private Integer jobId;
        
        public ApplyId() {}
        
        public ApplyId(Integer candidateId, Integer jobId) {
            this.candidateId = candidateId;
            this.jobId = jobId;
        }
        
        // equals and hashCode methods
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            
            ApplyId applyId = (ApplyId) o;
            
            if (!candidateId.equals(applyId.candidateId)) return false;
            return jobId.equals(applyId.jobId);
        }
        
        @Override
        public int hashCode() {
            int result = candidateId.hashCode();
            result = 31 * result + jobId.hashCode();
            return result;
        }
        
        // Getters and setters
        public Integer getCandidateId() {
            return candidateId;
        }
        
        public void setCandidateId(Integer candidateId) {
            this.candidateId = candidateId;
        }
        
        public Integer getJobId() {
            return jobId;
        }
        
        public void setJobId(Integer jobId) {
            this.jobId = jobId;
        }
    }
    
    // Getters and Setters for Apply
    public ApplyId getId() {
        return id;
    }

    public void setId(ApplyId id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUploadCV() {
        return uploadCV;
    }

    public void setUploadCV(String uploadCV) {
        this.uploadCV = uploadCV;
    }
}