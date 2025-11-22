package com.popcorn.jrp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.jrp.aicore.GeminiCvService;
import com.popcorn.jrp.domain.dto.aicore.AiCvParsingResponse;
import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.entity.CandidateSectionEntity;
import com.popcorn.jrp.repository.CandidateRepository; // Giả định bạn có repo này
import com.popcorn.jrp.repository.CandidateSectionRepository; // Giả định bạn có repo này
import com.popcorn.jrp.service.AiCandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiCandidateServiceImpl implements AiCandidateService {

    private final GeminiCvService geminiCvService;
    private final CandidateRepository candidateRepository;
    private final CandidateSectionRepository candidateSectionRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public void generateCvForCandidate(Long candidateId, String rawCvText) {
//        // 1. Gọi AI để lấy dữ liệu đã cấu trúc
//        AiCvParsingResponse aiData = geminiCvService.parseCvFromText(rawCvText);
//
//        // 2. Lấy Candidate hiện tại từ DB
//        CandidateEntity candidate = candidateRepository.findById(candidateId)
//                .orElseThrow(() -> new RuntimeException("Candidate not found"));
//
//        // 3. Update thông tin chung (CandidateEntity)
//        candidate.setName(aiData.getFullName());
//        candidate.setDesignation(aiData.getDesignation());
//        candidate.setEmail(aiData.getEmail());
//        candidate.setPhone(aiData.getPhone());
//        candidate.setDescription(aiData.getSummary());
//
//        if (aiData.getSkills() != null) {
//            candidate.setSkills(aiData.getSkills()); // Entity đang dùng List<String> + Converter
//        }
//        if (aiData.getLanguages() != null) {
//            candidate.setLanguages(aiData.getLanguages());
//        }
//
//        candidateRepository.save(candidate);
//
//        // 4. Update các Section (CandidateSectionEntity)
//        // Cách tiếp cận: Xóa các section cũ để tạo lại từ đầu (tùy nghiệp vụ)
//        // Hoặc bạn có thể check trùng lặp nếu muốn giữ lại cái cũ.
//        // Ở đây tôi demo cách xóa cũ thêm mới cho sạch.
//
//        // candidateSectionRepository.deleteAllByCandidateId(candidateId); // Cần implement trong Repo
//
//        if (aiData.getSections() != null) {
//            for (AiCvParsingResponse.AiSectionDto sectionDto : aiData.getSections()) {
//                CandidateSectionEntity section = CandidateSectionEntity.builder()
//                        .candidate(candidate)
//                        .category(sectionDto.getCategory()) // EXPERIENCE, EDUCATION...
//                        .title(sectionDto.getTitle())
//                        .organization(sectionDto.getOrganization())
//                        .text(sectionDto.getDescription())
//                        .startTime(parseDate(sectionDto.getStartDate()))
//                        .endTime(parseDate(sectionDto.getEndDate()))
//                        .build();
//
//                candidateSectionRepository.save(section);
//            }
//        }
    }

    @Transactional
    public AiCvParsingResponse improveAndReturnCv(Long candidateId) {
        // 1. Lấy dữ liệu gốc từ DB
        CandidateEntity candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        List<CandidateSectionEntity> sections = candidateSectionRepository.findByCandidateId(candidateId);

        // 2. Tạo Object trung gian để gửi cho AI (Dùng lại AiCvParsingResponse làm DTO input luôn cũng được)
        AiCvParsingResponse currentData = new AiCvParsingResponse();
        currentData.setFullName(candidate.getName());
        currentData.setDesignation(candidate.getDesignation());
        currentData.setSummary(candidate.getDescription());
        currentData.setSkills(candidate.getSkills());
        currentData.setEmail(candidate.getEmail());
        currentData.setPhone(candidate.getPhone());
        currentData.setLanguages(candidate.getLanguages());

        // Map Sections
        List<AiCvParsingResponse.AiSectionDto> sectionDtos = sections.stream().map(s -> {
            AiCvParsingResponse.AiSectionDto dto = new AiCvParsingResponse.AiSectionDto();
            dto.setCategory(s.getCategory());
            dto.setTitle(s.getTitle());
            dto.setOrganization(s.getOrganization());
            dto.setDescription(Collections.singletonList(s.getText())); // Text cũ sơ sài
            dto.setStartDate(s.getStartTime() != null ? s.getStartTime().toString() : null);
            dto.setEndDate(s.getEndTime() != null ? s.getEndTime().toString() : null);
            return dto;
        }).toList();
        currentData.setSections(sectionDtos);

        try {
            // 3. Gọi AI để cải thiện
            String inputJson = objectMapper.writeValueAsString(currentData);
            AiCvParsingResponse improvedData = geminiCvService.improveCvContent(inputJson);

            // 4. CẬP NHẬT NGƯỢC LẠI VÀO DATABASE (Quan trọng: Để lần sau lấy ra là bản xịn)
            candidate.setDescription(improvedData.getSummary());
            candidate.setDesignation(improvedData.getDesignation());
            // Update Skills nếu AI gợi ý lại
            if (improvedData.getSkills() != null && !improvedData.getSkills().isEmpty()) {
                candidate.setSkills(improvedData.getSkills());
            }
            candidateRepository.save(candidate);

            // Update Sections: Cách đơn giản nhất là Xóa cũ -> Tạo mới (hoặc Update từng dòng nếu có ID map)
            // Ở đây demo update text
            // Lưu ý: Logic map lại section khá phức tạp nếu ko có ID.
            // Để đơn giản, ta giả sử AI trả về thứ tự y hệt, hoặc ta xóa hết insert lại.
            // Code demo: Xóa hết section cũ, insert section mới đã cải thiện
            candidateSectionRepository.deleteAllByCandidateId(candidateId);

            for (AiCvParsingResponse.AiSectionDto secDto : improvedData.getSections()) {

                String descriptionText = "";
                if (secDto.getDescription() != null) {
                    descriptionText = String.join("\n", secDto.getDescription());
                }

                CandidateSectionEntity newSec = CandidateSectionEntity.builder()
                        .candidate(candidate)
                        .category(secDto.getCategory())
                        .title(secDto.getTitle())
                        .organization(secDto.getOrganization())
                        .text(descriptionText) // Text xịn sò AI viết
                        .startTime(secDto.getStartDate() != null ? LocalDate.parse(secDto.getStartDate()) : null)
                        .endTime(secDto.getEndDate() != null ? LocalDate.parse(secDto.getEndDate()) : null)
                        .build();
                candidateSectionRepository.save(newSec);
            }

            // 5. Trả về JSON xịn cho Frontend
            return improvedData;

        } catch (Exception e) {
            throw new RuntimeException("Error processing CV: " + e.getMessage());
        }
    }

    // Helper parse date
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty() || dateStr.equalsIgnoreCase("Present")) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE); // YYYY-MM-DD
        } catch (Exception e) {
            return null; // Handle lỗi parse date nếu AI trả về format lạ
        }
    }
}