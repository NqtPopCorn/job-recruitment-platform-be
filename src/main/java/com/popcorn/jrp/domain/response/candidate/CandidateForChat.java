package com.popcorn.jrp.domain.response.candidate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateForChat {
    private Long id;
    private Long userId;
    private String avatar;
    private String name;
    private String email;
}
