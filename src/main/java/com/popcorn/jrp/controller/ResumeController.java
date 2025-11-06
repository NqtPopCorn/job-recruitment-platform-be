package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.candidate.UpdateResumeDto;
import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.ApiResultsResponse;
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

        /**
         * Lấy chi tiết một CV
         * GET /api/v1/resume/:id
         */
        @GetMapping("/candidate/{candidateId}")
        public ResponseEntity<ApiResultsResponse<ResumeResponseDto>> getListResumeByCandidateId(
                        @PathVariable("candidateId") Long candidateId) {
                List<ResumeResponseDto> result = resumeService.getResumesByCandidateId(candidateId);

                ApiResultsResponse<ResumeResponseDto> response = ApiResultsResponse.<ResumeResponseDto>builder()
                                .statusCode(HttpStatus.OK.value()) // 200
                                .message("Lấy chi tiết hồ sơ công việc thành công!")
                                .results(result)
                                .build();

                return ResponseEntity.ok(response);
        }
}
