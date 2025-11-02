package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.employer.CreateEmployerDto;
import com.popcorn.jrp.domain.request.employer.EmployerQueryParameters;
import com.popcorn.jrp.domain.request.employer.UpdateEmployerDto;
import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.ApiResultsResponse;
import com.popcorn.jrp.domain.response.common.IndustryLabelValueDto;
import com.popcorn.jrp.domain.response.employer.*;
import com.popcorn.jrp.service.EmployerService;
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
}
