package com.popcorn.jrp.domain.response.employer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RelatedJobDto {
    private String id;
    private String jobTitle;
    private String location;
    private String industry;
    private JobSalaryDto salary;
}
