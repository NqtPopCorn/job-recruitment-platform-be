package com.popcorn.jrp.domain.response.job;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Destination;

import com.popcorn.jrp.domain.response.employer.EmployerResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDto {
    private Long id;
    private String logo;
    private String title;
    private EmployerResponse company;
    private String location;
    private String industry;
    private int quantity;
    private String country;
    private String city;

    @Builder.Default
    private int applications = 0;

    @Builder.Default
    private List<String> jobTypes = new ArrayList<>();

    private boolean status;
}
