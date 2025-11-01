package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.candidate.UpdateResumeDto;
import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.candidate.ResumeResponseDto;
import com.popcorn.jrp.service.ResumeService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    // CREATE RESUME IS IN CANDIDATE UPLOAD CONTROLLER

    // find pageable resumes by job id through application entity
    // path: /api/v1/resume&jobId=123456&page=&size=1
    // => ApplicationController?

    /**
     * Lấy chi tiết một CV
     * GET /api/v1/resume/:id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<ResumeResponseDto>> getResumeById(
            @PathVariable("id") Long resumeId
    ) {
        ResumeResponseDto result = resumeService.getResumeById(resumeId);

        var response = ApiDataResponse.<ResumeResponseDto>builder()
                .statusCode(HttpStatus.OK.value()) // 200
                .message("Lấy chi tiết hồ sơ thành công!")
                .data(result)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     *  Cập nhật thông tin CV
     * PUT /api/v1/resume/:id
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<ResumeResponseDto>> updateResume(
            @PathVariable("id") Long resumeId,
            @RequestBody UpdateResumeDto updateDto
    ) {
        ResumeResponseDto result = resumeService.updateResume(resumeId, updateDto);

        var response = ApiDataResponse.<ResumeResponseDto>builder()
                .statusCode(HttpStatus.OK.value()) // 200
                .message("Cập nhật hồ sơ thành công!")
                .data(result)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Xóa một CV
     * DELETE /api/v1/resume/:id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Object>> deleteResume(
            @PathVariable("id") Long resumeId
    ) {
        resumeService.deleteResume(resumeId);

        var response = ApiDataResponse.builder()
                .statusCode(HttpStatus.OK.value()) // 200
                .message("Xóa hồ sơ thành công!")
                .data(null) // data là null
                .build();

        return ResponseEntity.ok(response);
    }
}
