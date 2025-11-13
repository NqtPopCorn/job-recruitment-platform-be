package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.candidate.CreateCandidateAboutDto;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateAboutDto;
import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiResultsResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateAboutDto;
import com.popcorn.jrp.domain.response.candidate.CandidateSectionBlockDto;
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
        public ResponseEntity<ApiDataResponse<CandidateSectionBlockDto>> createCandidateAbout(
                        @Valid @RequestBody CreateCandidateAboutDto createDto) {

                CandidateSectionBlockDto newSection = candidateAboutService.createCandidateAbout(createDto);

                ApiDataResponse<CandidateSectionBlockDto> response = ApiDataResponse.<CandidateSectionBlockDto>builder()
                                .statusCode(HttpStatus.CREATED.value())
                                .message("Successfully created candidate about.")
                                .data(newSection)
                                .build();

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        /**
         * Endpoint 2: Cập nhật một mục thông tin.
         * PATCH /api/v1/candidate-about/:id
         */
        @PatchMapping("/{id}")
        public ResponseEntity<ApiDataResponse<CandidateSectionBlockDto>> updateCandidateAbout(
                        @PathVariable("id") Long id,
                        @Valid @RequestBody UpdateCandidateAboutDto updateDto) {

                CandidateSectionBlockDto updatedSection = candidateAboutService.updateCandidateAbout(id, updateDto);

                ApiDataResponse<CandidateSectionBlockDto> response = ApiDataResponse.<CandidateSectionBlockDto>builder()
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
        public ResponseEntity<ApiNoDataResponse> deleteCandidateAbout(@PathVariable("id") Long id) {

                candidateAboutService.deleteCandidateAbout(id);

                ApiNoDataResponse response = ApiNoDataResponse.builder()
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
        public ResponseEntity<ApiResultsResponse<CandidateSectionCategoryDto>> getSectionsByCandidateId(
                        @PathVariable("candidateId") Long candidateId) {

                List<CandidateSectionCategoryDto> sections = candidateAboutService
                                .getCandidateSectionsByCandidateId(candidateId);

                ApiResultsResponse<CandidateSectionCategoryDto> response = ApiResultsResponse
                                .<CandidateSectionCategoryDto>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy danh mục chứng chỉ của ứng viên theo id thành công!")
                                .results(sections)
                                .build();

                return ResponseEntity.ok(response);
        }
}