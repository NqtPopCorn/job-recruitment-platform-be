package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.response.candidate.ResumeResponseDto;
import com.popcorn.jrp.domain.request.candidate.UpdateResumeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface
 */
public interface ResumeService {

    /**
     * Lấy danh sách CV của ứng viên
     *
     * @param candidateId ID của ứng viên
     * @return Danh sách các DTO thông tin hồ sơ
     */
    List<ResumeResponseDto> getResumesByCandidateId(Long candidateId);

    /**
     * Tải lên CV mới
     *
     * @param candidateId ID của ứng viên
     * @param file        File được tải lên (từ form-data)
     * @param status      Trạng thái active (tùy chọn, từ form-data)
     * @return DTO của hồ sơ vừa được tạo
     */
    ResumeResponseDto createResume(Long candidateId, MultipartFile file, boolean status);

    /**
     * Lấy chi tiết một CV
     *
     * @param resumeId ID của hồ sơ (ResumeEntity)
     * @return DTO của hồ sơ
     */
    ResumeResponseDto getResumeById(Long resumeId);

    /**
     * Cập nhật thông tin CV
     *
     * @param resumeId  ID của hồ sơ (ResumeEntity)
     * @param updateDto DTO chứa thông tin cập nhật (fileName, status)
     * @return DTO của hồ sơ đã được cập nhật
     */
    ResumeResponseDto updateResume(Long resumeId, UpdateResumeDto updateDto);

    /**
     * Xóa một CV
     *
     * @param resumeId ID của hồ sơ (ResumeEntity)
     */
    void deleteResume(Long resumeId);
}