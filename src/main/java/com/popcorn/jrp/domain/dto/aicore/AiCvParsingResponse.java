package com.popcorn.jrp.domain.dto.aicore;

import lombok.Data;
import java.util.List;

@Data
public class AiCvParsingResponse {
    // Mapping cho CandidateEntity
    private String fullName;
    private String designation; // Chức danh (VD: Java Developer)
    private String email;
    private String phone;
    private String summary; // Mapping vào field 'description'
    private List<String> skills;
    private List<String> languages;

    // Mapping cho CandidateSectionEntity
    private List<AiSectionDto> sections;

    @Data
    public static class AiSectionDto {
        private String category; // EXPERIENCE, EDUCATION, PROJECT, CERTIFICATION
        private String title;    // Role / Degree
        private String organization; // Company / School
        private String startDate; // Format YYYY-MM-DD
        private String endDate;   // Format YYYY-MM-DD or 'Present'
        private List<String> description; // Mapping vào field 'text'
    }
}