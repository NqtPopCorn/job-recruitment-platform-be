package com.popcorn.jrp.domain.request.candidate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO để tạo một "candidate section" mới.
 * Tương ứng với endpoint: POST /api/v1/candidate-about
 */
@Data
@NoArgsConstructor
public class CreateCandidateAboutDto {

    @NotBlank(message = "Candidate ID không được để trống")
    private Long candidateId;

    @NotBlank(message = "Category không được để trống")
    private String category;

    @NotBlank(message = "Title không được để trống")
    private String title;

    @NotBlank(message = "Organization không được để trống")
    private String organization;

    @NotNull(message = "Start time không được để trống")
    private Instant startTime;

    // endTime có thể null (ví dụ: công việc hiện tại)
    private Instant endTime;

    // text có thể null
    private String text;

    // Lưu ý: Trường "type" trong tài liệu (ví dụ: "CreateCandidateAboutDto")
    // thường là metadata và không cần đưa vào DTO.
}
