package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.candidate.CreateCandidateAboutDto;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateAboutDto;
import com.popcorn.jrp.domain.response.candidate.CandidateAboutDto;
import com.popcorn.jrp.domain.response.candidate.CandidateSectionCategoryDto;

import java.util.List;

/**
 * Interface service cho việc quản lý các mục thông tin (sections) của ứng viên.
 */
public interface CandidateAboutService {

    /**
     * Tạo một mục thông tin mới cho ứng viên.
     * Tương ứng với: POST /api/v1/candidate-about
     *
     * @param createDto DTO chứa thông tin để tạo mới.
     * @return DTO của mục thông tin vừa được tạo.
     */
    CandidateAboutDto createCandidateAbout(CreateCandidateAboutDto createDto);

    /**
     * Cập nhật một mục thông tin đã có của ứng viên.
     * Tương ứng với: PATCH /api/v1/candidate-about/:id
     *
     * @param id ID của mục thông tin cần cập nhật.
     * @param updateDto DTO chứa thông tin cập nhật.
     * @return DTO của mục thông tin sau khi đã cập nhật.
     */
    CandidateAboutDto updateCandidateAbout(Long id, UpdateCandidateAboutDto updateDto);

    /**
     * Xóa một mục thông tin của ứng viên.
     * Tương ứng với: DELETE /api/v1/candidate-about/:id
     *
     * @param id ID của mục thông tin cần xóa.
     */
    void deleteCandidateAbout(Long id);

    /**
     * Lấy tất cả các mục thông tin, đã gom nhóm theo danh mục, của một ứng viên.
     * Tương ứng với: GET /api/v1/candidate-about/details/candidate/:candidateId
     *
     * @param candidateId ID của ứng viên.
     * @return Danh sách các danh mục (category) đã được gom nhóm.
     */
    List<CandidateSectionCategoryDto> getCandidateSectionsByCandidateId(Long candidateId);
}
