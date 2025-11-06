package com.popcorn.jrp.domain.response.candidate;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.popcorn.jrp.domain.response.common.SocialMediaDto;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class CandidateDetailsResponse extends CandidateResponse {
    private Long userId;
    private LocalDate birthday;
    private String email;
    private String industry;
    private Integer experience;
    private String qualification;
    private String phone;
    private BigDecimal currentSalary;
    private BigDecimal expectedSalary;
    private String currency;
    private String description;
    private List<String> languages;
    private List<SocialMediaDto> socialMedias;
}
