package com.ITworks.backend.repository;

import com.ITworks.backend.entity.Apply;
import com.ITworks.backend.entity.Apply.ApplyId;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, ApplyId> {

    @Query("SELECT a FROM Apply a WHERE a.id.jobId = :jobId")
    List<Apply> findByJobId(@Param("jobId") Integer jobId);
    
    @Query("SELECT a FROM Apply a WHERE a.id.candidateId = :candidateId")
    List<Apply> findByCandidateId(@Param("candidateId") Integer candidateId);
    
    @Query("SELECT a FROM Apply a WHERE a.id.candidateId = :candidateId AND a.id.jobId = :jobId")
    Optional<Apply> findByCandidateIdAndJobId(@Param("candidateId") Integer candidateId, @Param("jobId") Integer jobId);
}