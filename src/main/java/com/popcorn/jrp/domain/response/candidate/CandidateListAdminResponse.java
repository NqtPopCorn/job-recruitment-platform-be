package com.popcorn.jrp.domain.response.candidate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class CandidateListAdminResponse {
    private String id;
    private String avatar;
    private String name;
    private String designation;
    private String location;
    private String country;
    private String city;
    private Double hourlyRate;
    private List<String> tags;      // tương ứng với "skills" trong entity
    private String category;        // tương ứng với "industry"
    private String gender;
    private LocalDateTime createdAt;
    private boolean status;
    private boolean isDeleted;
}
