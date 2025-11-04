package com.popcorn.jrp.domain.response.candidate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponseDto {
    private Long id;
    private Long candidateId;
    private String fileName;
    private boolean status;
    private String url;
}
