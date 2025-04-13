package com.ITworks.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COMPANY")
public class Company {
    
    @Id
    @Column(length = 13)
    @Pattern(regexp = "[0-9]+", message = "Tax number must contain only digits")
    private String taxNumber;
    
    @Column(nullable = false, name = "MgrEmployerID")
    private Integer mgrEmployerId;
    
    @Column(nullable = false, length = 100)
    private String companyName;
    
    @Column(nullable = false, length = 50)
    private String cNationality;
    
    @Column(nullable = false)
    @Pattern(regexp = "(https?://).*", message = "Website URL must start with http:// or https://")
    private String website;
    
    @Column(nullable = false, length = 100)
    private String industry;
    
    @Column(nullable = false)
    private Integer companySize;
    
    @Lob
    @Column(nullable = false)
    private byte[] logo;
    
    @Column(nullable = false, columnDefinition = "nvarchar(max)")
    private String description;
    
    @Column(nullable = false)
    private String packageName;
    
    @ManyToOne
    @JoinColumn(name = "packageName", referencedColumnName = "packageName", insertable = false, updatable = false)
    private Package subscriptionPackage;
    
    @OneToOne
    @JoinColumn(name = "mgrEmployerId", referencedColumnName = "employerId", insertable = false, updatable = false)
    private Employer manager;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<Employer> employers = new HashSet<>();
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<Job> jobs = new HashSet<>();
    
    // Getters and Setters
    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public Integer getMgrEmployerId() {
        return mgrEmployerId;
    }

    public void setMgrEmployerId(Integer mgrEmployerId) {
        this.mgrEmployerId = mgrEmployerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCNationality() {
        return cNationality;
    }

    public void setCNationality(String cNationality) {
        this.cNationality = cNationality;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Integer getCompanySize() {
        return companySize;
    }

    public void setCompanySize(Integer companySize) {
        this.companySize = companySize;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Package getSubscriptionPackage() {
        return subscriptionPackage;
    }

    public void setSubscriptionPackage(Package subscriptionPackage) {
        this.subscriptionPackage = subscriptionPackage;
    }

    public Employer getManager() {
        return manager;
    }

    public void setManager(Employer manager) {
        this.manager = manager;
    }

    public Set<Employer> getEmployers() {
        return employers;
    }

    public void setEmployers(Set<Employer> employers) {
        this.employers = employers;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }
}