package com.ITworks.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "[PACKAGE]")
public class Package {
    
    @Id
    @Column(name = "PackageName")
    private String packageName;
    
    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal cost;
    
    @Column(nullable = false, columnDefinition = "nvarchar(max)")
    private String description;
    
    @OneToMany(mappedBy = "packageName", cascade = CascadeType.ALL)
    private Set<Company> companies = new HashSet<>();
    
    // Getters and Setters
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }
}