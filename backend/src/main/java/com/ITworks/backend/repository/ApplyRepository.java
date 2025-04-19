package com.ITworks.backend.repository;

import com.ITworks.backend.entity.Apply;
import com.ITworks.backend.entity.Apply.ApplyId;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, ApplyId> {
    List<Apply> findByJobId(Integer jobId);
    Optional<Apply> findByCandidateIdAndJobId(Integer candidateId, Integer jobId);
}