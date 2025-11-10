package com.popcorn.jrp.domain.request.application;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationRequestDto {
    @NotNull(message = "Candidate ID must not be null!")
    private Long candidateId;

    @NotNull(message = "Job ID must not be null!")
    private Long jobId;

    @NotNull(message = "Resume ID must not be null!")
    private Long resumeId;

    private String coverLetter;
}
