package com.ITworks.backend.service.impl;

import com.ITworks.backend.dto.Login.*;

import com.ITworks.backend.entity.Candidate;
import com.ITworks.backend.entity.Employer;
import com.ITworks.backend.entity.User;
import com.ITworks.backend.repository.CandidateRepository;
import com.ITworks.backend.repository.EmployerRepository;
import com.ITworks.backend.repository.UserRepository;
import com.ITworks.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CandidateRepository candidateRepository;
    
    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // Tìm user theo username
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new BadCredentialsException("Tên đăng nhập hoặc mật khẩu không đúng"));
        
        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Tên đăng nhập hoặc mật khẩu không đúng");
        }
        
        // Tạo response
        LoginResponseDTO response = new LoginResponseDTO();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setProfilePicture(user.getProfilePicture());
        
        // Xác định loại người dùng
        if (loginRequest.getUserType() == LoginRequestDTO.UserType.CANDIDATE) {
            Optional<Candidate> candidate = candidateRepository.findById(user.getId());
            if (candidate.isEmpty()) {
                throw new BadCredentialsException("Tài khoản này không phải là ứng viên");
            }
            response.setUserType("CANDIDATE");
            response.setTypeId(user.getId()); // CandidateID = UserID
        } else if (loginRequest.getUserType() == LoginRequestDTO.UserType.EMPLOYER) {
            Optional<Employer> employer = employerRepository.findById(user.getId());
            if (employer.isEmpty()) {
                throw new BadCredentialsException("Tài khoản này không phải là nhà tuyển dụng");
            }
            response.setUserType("EMPLOYER");
            response.setTypeId(user.getId()); // EmployerID = UserID
        }
        
        return response;
    }
}