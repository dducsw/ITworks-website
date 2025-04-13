package com.ITworks.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "[USER]")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(unique = true, nullable = false)
    @Email(message = "Email should be valid")
    private String email;
    
    @Column(nullable = false)
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
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    @Past(message = "Birth date must be in the past")
    private LocalDate bDate;
    
    @Column(nullable = false, columnDefinition = "nvarchar(max)")
    private String bio;
    
    @Column(nullable = false, length = 20)
    @Pattern(regexp = "0[0-9]*", message = "Phone number must start with 0 and contain only digits")
    private String phoneNum;
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBDate() {
        return bDate;
    }

    public void setBDate(LocalDate bDate) {
        this.bDate = bDate;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}