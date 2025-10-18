package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.mapper.PageMapper;
import com.popcorn.jrp.domain.request.CandidateSearchRequest;
import com.popcorn.jrp.domain.request.CreateCandidateDto;
import com.popcorn.jrp.domain.request.UpdateCandidateDto;
import com.popcorn.jrp.domain.response.*;
import com.popcorn.jrp.service.CandidateService;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidate")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    // 1️⃣ GET PAGINATED LIST
    @GetMapping
    public @ResponseBody ApiPageResponse<CandidateResponse> getCandidates(
            @Nullable Pageable pageable,
            @Nullable CandidateSearchRequest candidateSearchRequest
    ) {

        var body = new PageMapper<CandidateResponse>().toApiPageResponse(candidateService.getCandidates(candidateSearchRequest, pageable));
        body.setMessage("Success");
        body.setStatusCode(200);
        return body;
    }

    // 2️⃣ CREATE NEW CANDIDATE
    @PostMapping
    public ResponseEntity<ApiDataResponse<CandidateDetailsResponse>> createCandidate(
            @RequestBody CreateCandidateDto dto
    ) {
        var res = candidateService.createCandidate(dto);
        return ResponseEntity.ok(ApiDataResponse.<CandidateDetailsResponse>builder()
                .data(res)
                .message("Success")
                .statusCode(200)
                .build());
    }

    // 3️⃣ GET DETAIL BY CANDIDATE ID
    @GetMapping("/details/{id}")
    public ResponseEntity<ApiDataResponse<CandidateDetailsResponse>> getCandidateById(
            @PathVariable String id
    ) {
        var res = candidateService.getCandidateById(id);
        return ResponseEntity.ok(ApiDataResponse.<CandidateDetailsResponse>builder()
                .data(res)
                .message("Success")
                .statusCode(200)
                .build());
    }

    // 4️⃣ GET DETAIL BY USER ID
    @GetMapping("/details/user/{userId}")
    public ResponseEntity<ApiDataResponse<CandidateDetailsResponse>> getCandidateByUserId(
            @PathVariable String userId
    ) {
        var data = candidateService.getCandidateByUserId(userId);
        return ResponseEntity.ok(ApiDataResponse.<CandidateDetailsResponse>builder()
                        .data(data)
                        .message("Success")
                        .statusCode(200)
                .build());
    }

    // 6️⃣ GET INDUSTRY LIST
    @GetMapping("/industry-list")
    public @ResponseBody ApiDataResponse<List<String>> getIndustryList() {
        return ApiDataResponse.<List<String>>builder()
                .statusCode(200)
                .message("Success")
                .data(candidateService.getIndustryList())
                .build();
    }

    // 7️⃣ GET SKILL LIST
    @GetMapping("/skill-list")
    public @ResponseBody ApiDataResponse<List<String>> getSkillList() {
        return ApiDataResponse.<List<String>>builder()
                .statusCode(200)
                .message("Success")
                .data(candidateService.getSkillList())
                .build();
    }

    // 8️⃣ UPDATE CANDIDATE (PATCH)
    @PatchMapping("/{id}")
    public @ResponseBody ApiDataResponse<CandidateDetailsResponse> updateCandidate(
            @PathVariable String id,
            @RequestBody UpdateCandidateDto dto
    ) {
        var data = candidateService.updateCandidate(id, dto);
        return ApiDataResponse.<CandidateDetailsResponse>builder()
                .message("Success")
                .statusCode(200)
                .data(data)
                .build();
    }

    // 9️⃣ SOFT DELETE CANDIDATE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<SoftDeleteCandidateResponse>> softDeleteCandidate(
            @PathVariable String id
    ) {
        var data = candidateService.softDeleteCandidate(id);
        return ResponseEntity.ok(ApiDataResponse.<SoftDeleteCandidateResponse>builder()
                        .message("Success")
                        .statusCode(200)
                        .data(data)
                        .build());
    }
}

