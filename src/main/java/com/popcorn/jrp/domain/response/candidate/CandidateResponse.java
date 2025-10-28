package com.popcorn.jrp.domain.response.candidate;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CandidateResponse {
    private Long id;
    private String avatar;
    private String name;
    private String designation;
    private String location;
    private Double hourlyRate;
    private List<String> tags; // skills
    private String category; // industry
    private String gender;
    private String createdAt;
    private Boolean status;
}
