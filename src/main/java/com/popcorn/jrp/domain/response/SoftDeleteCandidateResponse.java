package com.popcorn.jrp.domain.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoftDeleteCandidateResponse {
    private String id;
    private String name;
    private String designation;
    private Boolean status;
    private String updatedAt;
}

