package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.employer.CreateEmployerDto;
import com.popcorn.jrp.domain.request.employer.EmployerQueryParameters;
import com.popcorn.jrp.domain.request.employer.UpdateEmployerDto;
import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.ApiResultsResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateResponse;
import com.popcorn.jrp.domain.response.candidate.NotificationResponse;
import com.popcorn.jrp.domain.response.common.IndustryLabelValueDto;
import com.popcorn.jrp.domain.response.employer.*;
import com.popcorn.jrp.service.ApplicationService;
import com.popcorn.jrp.service.EmployerService;
import com.popcorn.jrp.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller để quản lý thông tin Công ty (Nhà tuyển dụng).
 * Vẫn sử dụng endpoint /api/v1/company theo yêu cầu.
 */
@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
public class CompanyController {

        private final EmployerService employerService;
        private  final ApplicationService applicationService;
        private  final NotificationService notificationService;

        /**
         * 1. GET LIST PAGINATION COMPANY
         * GET /api/v1/company
         */
        @GetMapping
        public ResponseEntity<ApiPageResponse<EmployerPaginationDto>> getCompaniesPaginated(
                        @Valid EmployerQueryParameters queryParams,
                        Pageable pageable) {

                ApiPageResponse<EmployerPaginationDto> employerPage = employerService.getEmployersPaginated(queryParams,
                                pageable);

                return ResponseEntity.ok(employerPage);
        }

        /**
         * 3. GET DETAIL COMPANY BY ID
         * GET /api/v1/company/details/:id
         */
        @GetMapping("/details/{id}")
        public ResponseEntity<ApiDataResponse<EmployerDetailDto>> getCompanyById(@PathVariable("id") Long id) {

                EmployerDetailDto employer = employerService.getEmployerDetailsById(id);

                ApiDataResponse<EmployerDetailDto> response = ApiDataResponse.<EmployerDetailDto>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy thông tin công ty thành công!")
                                .data(employer)
                                .build();
                return ResponseEntity.ok(response);
        }

        /**
         * 4. GET DETAIL COMPANY BY USER ID
         * GET /api/v1/company/details/user/:id
         */
        @GetMapping("/details/user/{id}")
        public ResponseEntity<ApiDataResponse<EmployerDetailDto>> getCompanyByUserId(@PathVariable("id") Long userId) {

                EmployerDetailDto employer = employerService.getEmployerDetailsByUserId(userId);

                ApiDataResponse<EmployerDetailDto> response = ApiDataResponse.<EmployerDetailDto>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy thông tin công ty thành công!")
                                .data(employer)
                                .build();
                return ResponseEntity.ok(response);
        }

        /**
         * 5. GET RELATED JOBS BY COMPANY ID
         * GET /api/v1/company/related-jobs/:companyId
         */
        @GetMapping("/related-jobs/{companyId}")
        public ResponseEntity<ApiResultsResponse<RelatedJobDto>> getRelatedJobs(
                        @PathVariable("companyId") Long employerId) {

                List<RelatedJobDto> jobs = employerService.getRelatedJobsByEmployerId(employerId);

                ApiResultsResponse<RelatedJobDto> response = ApiResultsResponse.<RelatedJobDto>builder()
                                .message(jobs.size() + " jobs found")
                                .statusCode(HttpStatus.OK.value())
                                .results(jobs)
                                .build();

                return ResponseEntity.ok(response);
        }

        /**
         * 6. GET INDUSTRY LIST OF COMPANY
         * GET /api/v1/company/industry-list
         */
        @GetMapping("/industry-list")
        public ResponseEntity<ApiResultsResponse<IndustryLabelValueDto>> getIndustryList() {

                List<IndustryLabelValueDto> industries = employerService.getIndustryList();

                ApiResultsResponse<IndustryLabelValueDto> response = ApiResultsResponse.<IndustryLabelValueDto>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy danh sách danh mục của các ứng viên thành công!")
                                .results(industries)
                                .build();
                return ResponseEntity.ok(response);
        }

        /**
         * 8. PATCH COMPANY
         * PATCH /api/v1/company/:id
         */
        @PatchMapping("/{id}")
        public ResponseEntity<ApiDataResponse<EmployerDetailDto>> updateCompany(
                        @PathVariable("id") Long id,
                        @Valid @RequestBody UpdateEmployerDto updateDto) {

                EmployerDetailDto updatedEmployer = employerService.updateEmployer(id, updateDto);

                ApiDataResponse<EmployerDetailDto> response = new ApiDataResponse<>(
                                HttpStatus.OK.value(),
                                "Cập nhật công ty thành công!",
                                updatedEmployer);

                return ResponseEntity.ok(response);
        }

        /**
         * 9. SOFT DELETE COMPANY
         * DELETE /api/v1/company/:id
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiDataResponse<EmployerSoftDeleteDto>> softDeleteCompany(
                        @PathVariable("id") Long id) {

                EmployerSoftDeleteDto deletedInfo = employerService.softDeleteEmployer(id);

                ApiDataResponse<EmployerSoftDeleteDto> response = new ApiDataResponse<>(
                                HttpStatus.OK.value(),
                                "Xóa mềm công ty thành công!",
                                deletedInfo);

                return ResponseEntity.ok(response);
        }

        @PostMapping("/{employerId}/potential-candidates/{candidateId}/toggle")
        public ResponseEntity<ApiDataResponse<Boolean>> togglePotentialCandidate(
                        @PathVariable("employerId") Long employerId,
                        @PathVariable("candidateId") Long candidateId) {

                // toggle và nhận trạng thái hiện tại
                boolean added = employerService.togglePotentialCandidate(employerId, candidateId);

                ApiDataResponse<Boolean> res = ApiDataResponse.<Boolean>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message(added
                                                ? "Added potential candidate successfully"
                                                : "Removed potential candidate successfully")
                                .data(added)
                                .build();

                return ResponseEntity.ok(res);
        }

        /**
         * Kiểm tra xem ứng viên đã được lưu là tiềm năng hay chưa
         */
        @GetMapping("/{employerId}/potential-candidates/{candidateId}")
        public ResponseEntity<ApiDataResponse<Boolean>> checkPotentialCandidate(
                        @PathVariable("employerId") Long employerId,
                        @PathVariable("candidateId") Long candidateId) {

                boolean isPotential = employerService.checkPotentialCandidate(employerId, candidateId);

                ApiDataResponse<Boolean> res = ApiDataResponse.<Boolean>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message(isPotential
                                                ? "Candidate is in potential list"
                                                : "Candidate is not in potential list")
                                .data(isPotential)
                                .build();

                return ResponseEntity.ok(res);
        }

        /**
         * GET LIST PAGINATION POTENTIAL CANDIDATE OF EMPLOYER
         * GET /api/v1/company/{employerId}/potential-candidates
         */
        @GetMapping("/{employerId}/potential-candidates")
        public ResponseEntity<ApiPageResponse<CandidateResponse>> getPotentialCandidatesPaginated(
                        @PathVariable("employerId") Long employerId,
                        Pageable pageable,
                        @RequestParam String search) {

                ApiPageResponse<CandidateResponse> potentialCandidatePage = employerService
                                .getPotentialCanddiatesPaginated(employerId, search, pageable);
                return ResponseEntity.ok(potentialCandidatePage);
        }

    @GetMapping("/{employerId}/dashboard-stats")
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<EmployerDashboardStatsResponse> getDashboardStats(
            @PathVariable Long employerId) {

        EmployerDashboardStatsResponse data = employerService.getDashboardStats(employerId);

        return ApiDataResponse.<EmployerDashboardStatsResponse>builder()
                .data(data)
                .message("Lấy thống kê dashboard thành công!")
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @GetMapping("/dashboard-stats/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<EmployerDashboardStatsResponse> getDashboardStatsByUserId(
            @PathVariable Long userId) {

        EmployerDashboardStatsResponse data = employerService.getDashboardStatsByUserId(userId);

        return ApiDataResponse.<EmployerDashboardStatsResponse>builder()
                .data(data)
                .message("Lấy thống kê dashboard thành công!")
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    /**
     * GET ALL APPLICANTS FOR EMPLOYER WITH PAGINATION
     * GET /api/v1/company/employer?employerId={employerId}&page=0&size=10
     */
    @GetMapping("/employer")
    @ResponseStatus(HttpStatus.OK)
    public ApiPageResponse<RecentApplicantResponse> getAllApplicantsByEmployer(
            @RequestParam Long employerId,
            Pageable pageable) {

        ApiPageResponse<RecentApplicantResponse> response = applicationService
                .getAllApplicantsByEmployer(employerId, pageable);
        response.setMessage("Lấy danh sách tất cả ứng viên thành công!");
        response.setStatusCode(HttpStatus.OK.value());

        return response;
    }

    /**
     * GET ALL UNREAD NOTIFICATIONS
     * GET /api/v1/company/unread?userId={userId}
     */
    @GetMapping("/unread")
    @ResponseStatus(HttpStatus.OK)
    public ApiResultsResponse<NotificationResponse> getAllUnreadNotifications(
            @RequestParam Long userId) {

        List<NotificationResponse> notifications = notificationService
                .getAllUnreadNotifications(userId);

        return ApiResultsResponse.<NotificationResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Lấy danh sách thông báo chưa đọc thành công!")
                .results(notifications)
                .build();
    }
}
