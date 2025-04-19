package com.ITworks.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
// import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "[USER]")
@Getter
@Setter
public class User implements UserDetails {

    
    
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "Username", unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(name = "Email", unique = true, nullable = false)
    @Email(message = "Email should be valid")
    @Pattern(regexp = ".*@.*\\..*", message = "Email must be in valid format")
    private String email;
    
    @Column(name = "Password", nullable = false)
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    
    @Column(name = "FName", nullable = false, length = 50) 
    private String firstName;
    
    @Column(name = "LName", nullable = false, length = 50)
    private String lastName;
    
    @Column(name = "Created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Column(name = "Profile_Picture", nullable = false)
    private String profilePicture;
    
    @Column(name = "Address", nullable = false)
    private String address;
    
    @Column(name = "BDate", nullable = false)
    @Past(message = "Birth date must be in the past")
    private LocalDate bDate;

    @AssertTrue(message = "User must be at least 18 years old")
    private boolean isAdult() {
        return bDate != null && bDate.plusYears(18).isBefore(LocalDate.now());
    }
    
    @Column(name = "Bio", nullable = false, columnDefinition = "nvarchar(max)")
    private String bio;
    
    @Column(name = "PhoneNum", nullable = false, length = 20)
    @Pattern(regexp = "0[0-9]*", message = "Phone number must start with 0 and contain only digits")
    private String phoneNum;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Candidate candidate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Employer employer;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Add base role
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        // Add role based on profile type
        if (candidate != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));
        }
        
        if (employer != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYER"));
        }
        
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}