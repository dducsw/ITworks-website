package com.ITworks.backend.mapper;

import com.ITworks.backend.dto.Job.JobDTO;
import com.ITworks.backend.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobMapper {

    /**
     * Convert Job entity to JobDTO
     */
    JobDTO toJobDTO(Job job);

    /**
     * Convert a list of Job entities to a list of JobDTOs
     */
    List<JobDTO> toJobDTOList(List<Job> jobs);

    /**
     * Convert JobDTO to Job entity
     */
    @Mapping(target = "applications", ignore = true) // Ignore the association with applications
    @Mapping(target = "categories", ignore = true)   // Ignore the association with categories
    @Mapping(target = "requiredSkills", ignore = true) // Ignore the association with requiredSkills
    @Mapping(target = "employer", ignore = true)     // Ignore the association with employer
    @Mapping(target = "company", ignore = true)      // Ignore the association with company
    Job toEntity(JobDTO dto);
}
