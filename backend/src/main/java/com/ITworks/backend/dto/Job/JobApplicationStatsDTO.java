package com.ITworks.backend.dto.Job;

import lombok.Data;
import java.time.LocalDate;

@Data
public class JobApplicationStatsDTO {
    private Integer jobId;
    private String jobName;
    private LocalDate createdDate;
    private Integer daNhan;
    private Integer tuChoi;
    private Integer choDuyet;
}
