package com.popcorn.jrp.domain.response.candidate;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoftDeleteCandidateResponse {
    private Long id;
    private String name;
    private String designation;
    private Boolean status;
    private LocalDateTime deletedAt;
}
