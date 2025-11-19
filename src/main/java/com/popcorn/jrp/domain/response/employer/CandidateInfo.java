package com.popcorn.jrp.domain.response.employer;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateInfo {
    String email;
    String phone;
    String location;
    Integer experience; // years
    List<String> skills;
    String educationLevel;
}
