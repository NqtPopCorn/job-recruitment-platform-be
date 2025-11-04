package com.popcorn.jrp.domain.response.candidate;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO đại diện cho đối tượng CandidateAbout đầy đủ.
 * Được sử dụng làm đối tượng 'data' trả về trong response của
 * endpoint POST (Create) và PATCH (Update).
 */
@Data
@NoArgsConstructor
public class CandidateAboutDto {
    private String candidateId;
    private String category;
    private String title;
    private String organization;
    private Instant startTime;
    private Instant endTime;
    private String text;
    private Instant createdAt;
    private Instant updatedAt;
}
