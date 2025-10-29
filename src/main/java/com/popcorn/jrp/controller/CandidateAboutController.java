package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.candidate.CreateCandidateAboutDto;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateAboutDto;
import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateAboutDto;
import com.popcorn.jrp.domain.response.candidate.CandidateSectionCategoryDto;
import com.popcorn.jrp.service.CandidateAboutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller để quản lý các mục (sections) thông tin của ứng viên.
 */
@RestController
@RequestMapping("/api/v1/candidate-about")
@RequiredArgsConstructor
public class CandidateAboutController {

    private final CandidateAboutService candidateAboutService;

    /**
     * Endpoint 1: Tạo một mục thông tin mới.
     * POST /api/v1/candidate-about
     */
    @PostMapping
    public ResponseEntity<ApiDataResponse<CandidateAboutDto>> createCandidateAbout(
            @Valid @RequestBody CreateCandidateAboutDto createDto) {

        CandidateAboutDto newSection = candidateAboutService.createCandidateAbout(createDto);

        var response = ApiDataResponse.<CandidateAboutDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully created candidate about.")
                .data(newSection)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint 2: Cập nhật một mục thông tin.
     * PATCH /api/v1/candidate-about/:id
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiDataResponse<CandidateAboutDto>> updateCandidateAbout(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateCandidateAboutDto updateDto) {

        CandidateAboutDto updatedSection = candidateAboutService.updateCandidateAbout(id, updateDto);

        var response = ApiDataResponse.<CandidateAboutDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully updated candidate about.")
                .data(updatedSection)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint 3: Xóa một mục thông tin.
     * DELETE /api/v1/candidate-about/:id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse> deleteCandidateAbout(@PathVariable("id") Long id) {

        candidateAboutService.deleteCandidateAbout(id);

        var response = ApiDataResponse.builder()
                .statusCode(200)
                .message("Successfully deleted candidate about.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint 4: Lấy danh sách các mục thông tin của ứng viên.
     * GET /api/v1/candidate-about/details/candidate/:candidateId
     *
     */
    @GetMapping("/details/candidate/{candidateId}")
    public ResponseEntity<ApiDataResponse<List<CandidateSectionCategoryDto>>> getSectionsByCandidateId(
            @PathVariable("candidateId") Long candidateId) {

        List<CandidateSectionCategoryDto> sections =
                candidateAboutService.getCandidateSectionsByCandidateId(candidateId);

        var response = new ApiDataResponse<>(
                HttpStatus.OK.value(),
                "Lấy danh mục của ứng viên theo id thành công!",
                sections
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}