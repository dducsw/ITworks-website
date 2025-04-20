package com.ITworks.backend.mapper;

import com.ITworks.backend.dto.Job.JobDTO;
import com.ITworks.backend.dto.Job.JobUpdateDTO;
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


    @Mapping(target = "requireExpYear", source = "jobUpdateDTO.requireExpYear")
    @Mapping(target = "salaryFrom", source = "jobUpdateDTO.salaryFrom")
    @Mapping(target = "salaryTo", source = "jobUpdateDTO.salaryTo")
    @Mapping(target = "contractType", source = "jobUpdateDTO.contractType")
    @Mapping(target = "expireDate", source = "jobUpdateDTO.expireDate")
    @Mapping(target = "jobDescription", source = "jobUpdateDTO.jobDescription")
    @Mapping(target = "jobName", source = "jobUpdateDTO.jobName")
    @Mapping(target = "jobStatus", source = "jobUpdateDTO.jobStatus")
    @Mapping(target = "jobType", source = "jobUpdateDTO.jobType")
    @Mapping(target = "level", source = "jobUpdateDTO.level")
    @Mapping(target = "location", source = "jobUpdateDTO.location")
    @Mapping(target = "quantity", source = "jobUpdateDTO.quantity")
    Job updateJobFromDTO(JobUpdateDTO jobUpdateDTO, Job job);
}
