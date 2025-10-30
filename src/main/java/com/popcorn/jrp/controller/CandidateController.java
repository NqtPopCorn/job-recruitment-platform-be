package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.candidate.CandidateSearchRequest;
import com.popcorn.jrp.domain.request.candidate.CreateCandidateDto;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateDto;
import com.popcorn.jrp.domain.response.*;
import com.popcorn.jrp.domain.response.candidate.CandidateDetailsResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateResponse;
import com.popcorn.jrp.domain.response.candidate.ResumeResponseDto;
import com.popcorn.jrp.domain.response.candidate.SoftDeleteCandidateResponse;
import com.popcorn.jrp.service.CandidateService;
import com.popcorn.jrp.service.ResumeService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidate")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;
    private final ResumeService resumeService;

    // GET PAGINATED LIST
    @GetMapping
    public @ResponseBody ApiPageResponse<CandidateResponse> getCandidates(
            @Nullable Pageable pageable,
            @Nullable CandidateSearchRequest candidateSearchRequest
    ) {

        var body = candidateService.getCandidates(candidateSearchRequest, pageable);
        body.setMessage("Success");
        body.setStatusCode(200);
        return body;
    }

    //  DELETED: CREATE NEW CANDIDATE
//    @PostMapping
//    public ResponseEntity<ApiDataResponse<CandidateDetailsResponse>> createCandidate(
//            @RequestBody CreateCandidateDto dto
//    ) {
//        var res = candidateService.createCandidate(dto);
//        return ResponseEntity.ok(ApiDataResponse.<CandidateDetailsResponse>builder()
//                .data(res)
//                .message("Success")
//                .statusCode(200)
//                .build());
//    }

    // GET DETAIL BY CANDIDATE ID
    @GetMapping("/details/{id}")
    public ResponseEntity<ApiDataResponse<CandidateDetailsResponse>> getCandidateById(
            @PathVariable Long id
    ) {
        var res = candidateService.getCandidateById(id);
        return ResponseEntity.ok(ApiDataResponse.<CandidateDetailsResponse>builder()
                .data(res)
                .message("Success")
                .statusCode(200)
                .build());
    }

    //  GET DETAIL BY USER ID
    @GetMapping("/details/user/{userId}")
    public ResponseEntity<ApiDataResponse<CandidateDetailsResponse>> getCandidateByUserId(
            @PathVariable Long userId
    ) {
        var data = candidateService.getCandidateByUserId(userId);
        return ResponseEntity.ok(ApiDataResponse.<CandidateDetailsResponse>builder()
                        .data(data)
                        .message("Success")
                        .statusCode(200)
                .build());
    }

    //  GET INDUSTRY LIST
    @GetMapping("/industry-list")
    public @ResponseBody ApiDataResponse<List<String>> getIndustryList() {
        return ApiDataResponse.<List<String>>builder()
                .statusCode(200)
                .message("Success")
                .data(candidateService.getIndustryList())
                .build();
    }

    //  GET SKILL LIST
    @GetMapping("/skill-list")
    public @ResponseBody ApiDataResponse<List<String>> getSkillList() {
        return ApiDataResponse.<List<String>>builder()
                .statusCode(200)
                .message("Success")
                .data(candidateService.getSkillList())
                .build();
    }

    //  UPDATE CANDIDATE (PATCH)
    @PatchMapping("/{id}")
    public @ResponseBody ApiDataResponse<CandidateDetailsResponse> updateCandidate(
            @PathVariable Long id,
            @RequestBody UpdateCandidateDto dto
    ) {
        var data = candidateService.updateCandidate(id, dto);
        return ApiDataResponse.<CandidateDetailsResponse>builder()
                .message("Success")
                .statusCode(200)
                .data(data)
                .build();
    }

    //  SOFT DELETE CANDIDATE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<SoftDeleteCandidateResponse>> softDeleteCandidate(
            @PathVariable Long id
    ) {
        var data = candidateService.softDeleteCandidate(id);
        return ResponseEntity.ok(ApiDataResponse.<SoftDeleteCandidateResponse>builder()
                        .message("Success")
                        .statusCode(200)
                        .data(data)
                        .build());
    }
}

