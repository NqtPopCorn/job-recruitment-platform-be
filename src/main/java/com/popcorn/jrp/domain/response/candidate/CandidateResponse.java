package com.popcorn.jrp.domain.response.candidate;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.popcorn.jrp.domain.response.common.SocialMediaDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CandidateResponse {
    private Long id;
    private String avatar;
    private String name;
    private String email;
    private String designation;
    private String location;
    private Double hourlyRate;
    private BigDecimal currentSalary;
    private BigDecimal expectedSalary;
    private String currency;
    private List<String> skills; // skills
    private String industry; // industry
    private String gender;
    private String country;
    private String city;
    private LocalDateTime createdAt;
    private boolean status;
}
