package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.mapper.CandidateMapper;
import com.popcorn.jrp.domain.request.candidate.CandidateSearchRequest;
import com.popcorn.jrp.domain.request.candidate.CreateCandidateDto;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateDto;
import com.popcorn.jrp.domain.response.*;
import com.popcorn.jrp.domain.response.candidate.*;
import com.popcorn.jrp.service.CandidateService;
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
@RequestMapping("/api/v1/candidate")
@RequiredArgsConstructor
public class CandidateController {

        private final CandidateService candidateService;

        // GET PAGINATED LIST
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public ApiPageResponse<CandidateResponse> getCandidates(
                        @Nullable Pageable pageable,
                        @Nullable CandidateSearchRequest candidateSearchRequest) {

                ApiPageResponse<CandidateResponse> body = candidateService.getCandidates(candidateSearchRequest,
                                pageable);
                body.setMessage("Lấy danh sách hồ sơ ứng viên phân trang thành công!");
                body.setStatusCode(HttpStatus.OK.value());
                return body;
        }

        // GET DETAIL BY CANDIDATE ID
        @GetMapping("/details/{id}")
        @ResponseStatus(HttpStatus.OK)
        public ApiDataResponse<CandidateDetailsResponse> getCandidateById(
                        @PathVariable Long id) {
                CandidateDetailsResponse data = candidateService.getCandidateById(id);
                return ApiDataResponse.<CandidateDetailsResponse>builder()
                                .data(data)
                                .message("Success")
                                .statusCode(HttpStatus.OK.value())
                                .build();
        }

        // GET DETAIL BY USER ID
        @GetMapping("/details/user/{userId}")
        @ResponseStatus(HttpStatus.OK)
        public ApiDataResponse<CandidateDetailsResponse> getCandidateByUserId(
                        @PathVariable Long userId) {
                CandidateDetailsResponse data = candidateService.getCandidateByUserId(userId);
                return ApiDataResponse.<CandidateDetailsResponse>builder()
                                .data(data)
                                .message("Success")
                                .statusCode(HttpStatus.OK.value())
                                .build();
        }

        // GET INDUSTRY LIST
        @GetMapping("/industry-list")
        public ResponseEntity<ApiResultsResponse<String>> getIndustryList() {
                ApiResultsResponse<String> response = ApiResultsResponse.<String>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy danh sách danh mục của các ứng viên thành công!")
                                .results(candidateService.getIndustryList())
                                .build();
                return ResponseEntity.ok(response);
        }

        // GET SKILL LIST
        @GetMapping("/skill-list")
        public ResponseEntity<ApiResultsResponse<String>> getSkillList() {
                ApiResultsResponse<String> response = ApiResultsResponse.<String>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy danh sách kỹ năng thành công!")
                                .results(candidateService.getSkillList())
                                .build();
                return ResponseEntity.ok(response);
        }

        // UPDATE CANDIDATE (PATCH)
        @PatchMapping("/{id}")
        public ResponseEntity<ApiDataResponse<CandidateDetailsResponse>> updateCandidate(
                        @PathVariable Long id,
                        @RequestBody UpdateCandidateDto dto) {
                CandidateDetailsResponse data = candidateService.updateCandidate(id, dto);
                ApiDataResponse<CandidateDetailsResponse> response = ApiDataResponse.<CandidateDetailsResponse>builder()
                                .message("Cập nhật hồ sơ ứng viên thành công!")
                                .statusCode(HttpStatus.OK.value())
                                .data(data)
                                .build();
                return ResponseEntity.ok(response);
        }

        // SOFT DELETE CANDIDATE
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiDataResponse<SoftDeleteCandidateResponse>> softDeleteCandidate(
                        @PathVariable Long id) {
                var data = candidateService.softDeleteCandidate(id);
                return ResponseEntity.ok(ApiDataResponse.<SoftDeleteCandidateResponse>builder()
                                .message("Success")
                                .statusCode(200)
                                .data(data)
                                .build());
        }
}
