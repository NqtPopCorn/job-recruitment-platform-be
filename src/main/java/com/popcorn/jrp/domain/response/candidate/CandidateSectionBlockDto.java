package com.popcorn.jrp.domain.response.candidate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO đại diện cho một khối thông tin chi tiết (ví dụ: một công việc, một học vấn).
 * Đây là một phần của response từ endpoint GET .../details/candidate/:candidateId
 */
@Data
@NoArgsConstructor
public class CandidateSectionBlockDto {

    private String id;
    private String meta;
    private String title;
    private String organization;

    // "time" trong response là một chuỗi đã định dạng (ví dụ: "Sep 01, 2022 - Jan 01, 2027")
    // nên ta dùng String thay vì Instant.
    private String time;

    private String text;
}
