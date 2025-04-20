package com.ITworks.backend.repositories;

import com.ITworks.backend.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    List<Skill> findBySkillNameContainingIgnoreCase(String keyword);
}