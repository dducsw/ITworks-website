package com.ITworks.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EMPLOYER")
@Getter
@Setter
public class Employer {
    
    @Id
    @Column(name = "EmployerID")
    private Integer employerId;
    
    @OneToOne
    @JoinColumn(name = "EmployerID")
    @MapsId
    private User user;
    
    @Column(name = "TaxNumber", nullable = false, length = 13)
    @Pattern(regexp = "^[0-9]{10}|[0-9]{13}$", message = "Tax number must be either 10 or 13 digits")
    private String taxNumber;
    
    @ManyToOne
    @JoinColumn(name = "TaxNumber", referencedColumnName = "TaxNumber", insertable = false, updatable = false)
    private Company company;
    
    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Job> jobs = new HashSet<>();
}