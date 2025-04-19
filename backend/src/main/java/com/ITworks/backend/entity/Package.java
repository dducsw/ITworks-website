package com.ITworks.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "[PACKAGE]")
@Getter
@Setter
public class Package {
    
    @Id
    @Column(name = "PackageName", length = 100)
    private String packageName;
    
    @Column(name = "Cost", nullable = false, precision = 18, scale = 2)
    private BigDecimal cost;
    
    @Column(name = "Description", nullable = false, columnDefinition = "nvarchar(max)")
    private String description;
    
    @OneToMany(mappedBy = "subscriptionPackage", cascade = CascadeType.ALL)
    private Set<Company> companies = new HashSet<>();
}