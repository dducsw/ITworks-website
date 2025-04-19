package com.ITworks.backend.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CANDIDATE")
public class Candidate {
    
    @Id
    private Integer candidateId;
    
    @OneToOne
    @JoinColumn(name = "CandidateId")
    @MapsId
    private User user;
    
    // @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    // private Set<WorkExperience> workExperiences = new HashSet<>();
    
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Apply> applications = new HashSet<>();
    
    // @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    // private Set<Favorite> favorites = new HashSet<>();
    
    // @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    // private Set<Feedback> feedbacks = new HashSet<>();
    
    // Getters and Setters
    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // public Set<WorkExperience> getWorkExperiences() {
    //     return workExperiences;
    // }

    // public void setWorkExperiences(Set<WorkExperience> workExperiences) {
    //     this.workExperiences = workExperiences;
    // }

    public Set<Apply> getApplications() {
        return applications;
    }

    public void setApplications(Set<Apply> applications) {
        this.applications = applications;
    }

    // public Set<Favorite> getFavorites() {
    //     return favorites;
    // }

    // public void setFavorites(Set<Favorite> favorites) {
    //     this.favorites = favorites;
    // }

    // public Set<Feedback> getFeedbacks() {
    //     return feedbacks;
    // }

    // public void setFeedbacks(Set<Feedback> feedbacks) {
    //     this.feedbacks = feedbacks;
    // }
}