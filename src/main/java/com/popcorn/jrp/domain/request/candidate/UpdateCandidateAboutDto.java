package com.popcorn.jrp.domain.request.candidate;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO để cập nhật một "candidate section" đã có.
 * Tương ứng với endpoint: PATCH /api/v1/candidate-about/:id
 * Tất cả các trường đều là optional.
 */
@Data
@NoArgsConstructor
public class UpdateCandidateAboutDto {

    private String category;

    private String title;

    private String organization;

    private Instant startTime;

    private Instant endTime;

    private String text;
}
