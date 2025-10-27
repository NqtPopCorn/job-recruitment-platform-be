package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.request.candidate.CreateCandidateAboutDto;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateAboutDto;
import com.popcorn.jrp.domain.response.candidate.CandidateAboutDto;
import com.popcorn.jrp.domain.response.candidate.CandidateSectionBlockDto;
import com.popcorn.jrp.domain.response.candidate.CandidateSectionCategoryDto;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.service.CandidateAboutService;

import java.util.List;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.entity.CandidateSectionEntity;
import com.popcorn.jrp.domain.mapper.CandidateSectionMapper;
import com.popcorn.jrp.repository.CandidateRepository;
import com.popcorn.jrp.repository.CandidateSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Tự động inject các dependency final qua constructor
public class CandidateAboutServiceV1 implements CandidateAboutService {

    private final CandidateSectionRepository candidateSectionRepository;
    private final CandidateRepository candidateRepository;
    private final CandidateSectionMapper candidateSectionMapper;

    @Override
    @Transactional
    public CandidateAboutDto createCandidateAbout(CreateCandidateAboutDto createDto) {
        CandidateEntity candidate = candidateRepository.findById(createDto.getCandidateId())
                .orElseThrow(() -> new NotFoundException(
                        "Candidate với ID: " + createDto.getCandidateId()
                ));
        CandidateSectionEntity newSection = candidateSectionMapper.toEntity(createDto);
        newSection.setCandidate(candidate);

        CandidateSectionEntity savedSection = candidateSectionRepository.save(newSection);

        return candidateSectionMapper.toCandidateAboutDto(savedSection);
    }

    @Override
    @Transactional
    public CandidateAboutDto updateCandidateAbout(Long id, UpdateCandidateAboutDto updateDto) {
        CandidateSectionEntity existingSection = candidateSectionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CandidateSection với ID: " + id));
        candidateSectionMapper.updateEntityFromDto(updateDto, existingSection);
        CandidateSectionEntity updatedSection = candidateSectionRepository.save(existingSection);
        return candidateSectionMapper.toCandidateAboutDto(updatedSection);
    }

    @Override
    @Transactional
    public void deleteCandidateAbout(Long id) {
        if (!candidateSectionRepository.existsById(id)) {
            throw new NotFoundException("CandidateSection với ID: " + id);
        }
        candidateSectionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateSectionCategoryDto> getCandidateSectionsByCandidateId(Long candidateId) {
        List<CandidateSectionEntity> allSections = candidateSectionRepository.findByCandidateId(candidateId);
        if (allSections.isEmpty()) {
            return List.of();
        }

        // 2. Gom nhóm các section theo `industry` (tức là `category`)
        Map<String, List<CandidateSectionEntity>> groupedByIndustry = allSections.stream()
                .collect(Collectors.groupingBy(CandidateSectionEntity::getIndustry));

        // 3. Chuyển đổi map đã gom nhóm thành List<CandidateSectionCategoryDto>
        return groupedByIndustry.entrySet().stream()
                .map(entry -> {
                    String categoryName = entry.getKey();
                    List<CandidateSectionEntity> sectionsInCategory = entry.getValue();

                    // Chuyển danh sách entity trong category thành danh sách block DTO
                    List<CandidateSectionBlockDto> blockList = candidateSectionMapper.toCandidateSectionBlockDtoList(sectionsInCategory);

                    // Tạo đối tượng Category DTO
                    CandidateSectionCategoryDto categoryDto = new CandidateSectionCategoryDto();
                    categoryDto.setCategory(categoryName);
                    categoryDto.setBlockList(blockList);

                    // TODO: Thêm logic để xác định themeColor dựa trên categoryName nếu cần
                    // Ví dụ: if (categoryName.equals("Works & Experiences")) categoryDto.setThemeColor("theme-blue");

                    if (categoryName.equals("Works & Experiences")) categoryDto.setThemeColor("theme-blue");
                    else if(categoryName.equals("'Awards")) categoryDto.setThemeColor("theme-yellow");
                    else categoryDto.setThemeColor("");

                    return categoryDto;
                })
                .collect(Collectors.toList());
    }
}
