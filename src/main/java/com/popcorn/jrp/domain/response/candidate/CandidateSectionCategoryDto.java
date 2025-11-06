package com.popcorn.jrp.domain.response.candidate;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO đại diện cho một danh mục (ví dụ: "Educations", "Works & Experiences")
 * chứa danh sách các khối thông tin.
 * Đây là một phần của response từ endpoint GET
 * .../details/candidate/:candidateId
 */
@Data
@NoArgsConstructor
public class CandidateSectionCategoryDto {

    private String category;
    private String themeColor;
    private List<CandidateSectionBlockDto> blockList;
}
