package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.candidate.CandidateSearchAdminRequest;
import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateDetailsAdminResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateListAdminResponse;
import com.popcorn.jrp.service.CandidateAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import jakarta.annotation.Nullable;

@RestController
@RequestMapping("/api/v1/admin/candidates")
@RequiredArgsConstructor
public class CandidateAdminController {
    private final CandidateAdminService candidateAdminService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiPageResponse<CandidateListAdminResponse> getCandidatesListAdmin(
            @Nullable Pageable pageable,
            @Nullable CandidateSearchAdminRequest candidateSearchRequest) {

        ApiPageResponse<CandidateListAdminResponse> response =
                candidateAdminService.getCandidatesListAdmin(candidateSearchRequest, pageable);
        System.out.println("vao day ne: " + candidateSearchRequest.getGender());
        System.out.println("Status value: " + candidateSearchRequest.getStatus());

        response.setMessage("Lấy danh sách hồ sơ ứng viên phân trang cho admin thành công!");
        response.setStatusCode(HttpStatus.OK.value());

        return response;
    }

    @GetMapping("/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<CandidateDetailsAdminResponse> getCandidateDetailsById(
            @PathVariable Long id) {

        CandidateDetailsAdminResponse data = candidateAdminService.getCandidateDetailsById(id);

        return ApiDataResponse.<CandidateDetailsAdminResponse>builder()
                .data(data)
                .message("Lấy hồ sơ ứng viên theo id thành công!")
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PatchMapping("/{id}/lock")
    @ResponseStatus(HttpStatus.OK)
    public ApiNoDataResponse lockCandidate(@PathVariable Long id) {
        return candidateAdminService.lockCandidate(id);
    }

}
