package com.popcorn.jrp.domain.response.job;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobTypeDto {
    private String styleClass; // time, level
    private String type; // Full Time, Intern
}