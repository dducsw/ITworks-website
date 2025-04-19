package com.ITworks.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "APPLY")
@Getter
@Setter
public class Apply {
    
    @EmbeddedId
    private ApplyId id;
    
    @MapsId("candidateId")
    @ManyToOne
    @JoinColumn(name = "CandidateID")
    private Candidate candidate;
    
    @MapsId("jobId")
    @ManyToOne
    @JoinColumn(name = "JobID")
    private Job job;
    
    @Column(name = "CoverLetter", columnDefinition = "nvarchar(max)")
    private String coverLetter;
    
    @Column(name = "Date", nullable = false)
    private LocalDateTime date = LocalDateTime.now();
    
    @Column(name = "Status", length = 50)
    private String status;
    
    @Column(name = "UploadCV")
    private String uploadCV;
    
    // Embedded ID class
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplyId implements Serializable {
        @Column(name = "CandidateID")
        private Integer candidateId;
        
        @Column(name = "JobID")
        private Integer jobId;
    }
}