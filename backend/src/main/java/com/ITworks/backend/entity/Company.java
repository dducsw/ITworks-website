package com.ITworks.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COMPANY")
@Getter
@Setter
public class Company {
    
    @Id
    @Column(name = "TaxNumber", length = 13)
    @Pattern(regexp = "[0-9]+", message = "Tax number must contain only digits")
    private String taxNumber;
    
    @Column(name = "MgrEmployerID", nullable = false)
    private Integer mgrEmployerId;
    
    @Column(name = "CompanyName", nullable = false, length = 100)
    private String companyName;
    
    @Column(name = "CNationality", nullable = false, length = 50)
    private String cNationality;
    
    @Column(name = "Website", nullable = false, length = 255)
    @Pattern(regexp = "(https?://).*", message = "Website URL must start with http:// or https://")
    private String website;
    
    @Column(name = "Industry", nullable = false, length = 100)
    private String industry;
    
    @Column(name = "CompanySize", nullable = false)
    private Integer companySize;
    
    @Lob
    @Column(name = "Logo", nullable = false, columnDefinition = "varbinary(max)")
    private byte[] logo;
    
    @Column(name = "Description", nullable = false, columnDefinition = "nvarchar(max)")
    private String description;
    
    @Column(name = "PackageName", nullable = false, length = 100)
    private String packageName;
    
    @ManyToOne
    @JoinColumn(name = "PackageName", referencedColumnName = "PackageName", insertable = false, updatable = false)
    private Package subscriptionPackage;
    
    @OneToOne
    @JoinColumn(name = "MgrEmployerID", referencedColumnName = "employerId", insertable = false, updatable = false)
    private Employer manager;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<Employer> employers = new HashSet<>();
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<Job> jobs = new HashSet<>();
}