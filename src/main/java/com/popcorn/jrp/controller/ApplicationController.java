package com.popcorn.jrp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.popcorn.jrp.domain.entity.ApplicationEntity;
import com.popcorn.jrp.domain.query.ApplicationQueryDto;
import com.popcorn.jrp.domain.request.application.CreateApplicationRequestDto;
import com.popcorn.jrp.domain.request.application.UpdateApplicationStatusRequest;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.ApiResponse;
import com.popcorn.jrp.domain.response.ApiResultsResponse;
import com.popcorn.jrp.domain.response.application.ApplicantResponseDto;
import com.popcorn.jrp.domain.response.application.ApplicationPageResponseDto;
import com.popcorn.jrp.domain.response.application.ApplicationResponseDto;
import com.popcorn.jrp.domain.response.candidate.CandidateResponse;
import com.popcorn.jrp.service.ApplicationService;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/application")
@RequiredArgsConstructor
@Slf4j
public class ApplicationController {

        private final ApplicationService applicationService;

        @PostMapping()
        public ResponseEntity<ApiResponse<ApplicationResponseDto>> createApplication(
                        @Valid @RequestBody CreateApplicationRequestDto request) {
                log.info("Received request: {}", request); // check xem Jackson đã bind chưa
                ApplicationResponseDto dto = applicationService.createApplication(request);
                ApiResponse<ApplicationResponseDto> res = ApiResponse.<ApplicationResponseDto>builder()
                                .statusCode(HttpStatus.CREATED.value())
                                .message("Ứng tuyển thành công!")
                                .data(dto)
                                .build();
                return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }

        @GetMapping("/check")
        public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkApply(
                        @RequestParam(name = "candidateId") Long candidateId,
                        @RequestParam(name = "jobId") Long jobId) {

                boolean hasApplied = applicationService.hasCandidateApplied(candidateId, jobId);
                ApiResponse<Map<String, Boolean>> res = ApiResponse.<Map<String, Boolean>>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Kiểm tra ứng tuyển thành công!")
                                .data(Map.of("hasApplied", hasApplied))
                                .build();
                return ResponseEntity.ok(res);
        }

        @GetMapping("/all/dashboard/candidate/{candidateId}")
        public ResponseEntity<ApiPageResponse<ApplicationPageResponseDto>> getApplications(
                        @PathVariable(name = "candidateId") Long candidateId,
                        Pageable pageable,
                        @ModelAttribute ApplicationQueryDto query) {
                Page<ApplicationPageResponseDto> page = applicationService.getApplicationsByCandidateId(candidateId,
                                pageable,
                                query);
                ApiPageResponse<ApplicationPageResponseDto> response = ApiPageResponse
                                .<ApplicationPageResponseDto>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy danh sách đơn ứng tuyển thành công!")
                                .meta(ApiPageResponse.Meta.builder()
                                                .totalItems(page.getTotalElements())
                                                .totalPages(page.getTotalPages())
                                                .currentPage(pageable.getPageNumber() + 1)
                                                .pageSize(pageable.getPageSize())
                                                .build())
                                .results(page.getContent())
                                .build();
                return ResponseEntity.ok(response);
        }

        @GetMapping("/applicants/job/{jobId}")
        public ResponseEntity<ApiResultsResponse<ApplicantResponseDto>> getApplicantsAppliedByJobId(
                        @PathVariable("jobId") Long jobId) {
                List<ApplicantResponseDto> dataList = applicationService.getApplicantsAppliedByJobId(jobId);
                ApiResultsResponse<ApplicantResponseDto> res = ApiResultsResponse.<ApplicantResponseDto>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Get applicants by job id successfully")
                                .results(dataList)
                                .build();
                return ResponseEntity.ok(res);
        }

        @PatchMapping("/{applicationId}/status")
        public ResponseEntity<ApiNoDataResponse> updateApplicantStatus(
                        @PathVariable Long applicationId,
                        @RequestBody UpdateApplicationStatusRequest request) {

                applicationService.updateApplicantStatus(applicationId, request.getStatus());

                ApiNoDataResponse res = ApiNoDataResponse.builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Cập nhật trạng thái ứng viên thành công!")
                                .build();

                return ResponseEntity.ok(res);
        }

}
