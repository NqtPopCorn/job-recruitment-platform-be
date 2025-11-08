package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.job.*;
import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.ApiResultsResponse;
import com.popcorn.jrp.domain.response.job.JobDashboardDto;
import com.popcorn.jrp.domain.response.job.JobDetailDto;
import com.popcorn.jrp.domain.response.job.JobResponseDto;
import com.popcorn.jrp.service.JobService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/job")
@RequiredArgsConstructor
public class JobController {

        private final JobService jobService;

        /**
         * 1. GET LIST PAGINATION JOB
         * GET /api/v1/job
         */
        @GetMapping
        public ResponseEntity<ApiPageResponse<JobResponseDto>> getJobsPaginated(
                        @PageableDefault(size = 10, page = 1, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                        @ModelAttribute JobQueryParameters queryParams) {
                ApiPageResponse<JobResponseDto> body = jobService.getJobsPaginated(queryParams, pageable);
                return ResponseEntity.ok(body);
        }

        /**
         * 3. GET Detail by Job ID
         * GET /api/v1/job/detail/:id
         */
        @GetMapping("/detail/{id}")
        public ResponseEntity<ApiDataResponse<JobDetailDto>> getJobById(
                        @PathVariable Long id) {
                JobDetailDto data = jobService.getJobById(id);
                return ResponseEntity.ok(ApiDataResponse.<JobDetailDto>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy thông tin chi tiết công việc thành công!")
                                .data(data)
                                .build());
        }

        /**
         * 4. GET List Category (Industry of Job)
         * GET /api/v1/job/category-list
         */
        @GetMapping("/category-list")
        public ResponseEntity<ApiResultsResponse<String>> getJobIndustryList() {
                List<String> data = jobService.getJobIndustryList();
                return ResponseEntity.ok(ApiResultsResponse.<String>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy danh sách danh mục thành công!")
                                .results(data)
                                .build());
        }

        /**
         * 5. GET List Primary Industry of Company
         * GET /api/v1/job/category-list/company/:id
         */
        @GetMapping("/category-list/company/{id}")
        public ResponseEntity<ApiResultsResponse<String>> getCompanyIndustryList(
                        @PathVariable Long id) {
                List<String> data = jobService.getCompanyIndustryListByCompanyId(id);
                return ResponseEntity.ok(ApiResultsResponse.<String>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy danh sách danh mục công ty thành công!")
                                .results(data)
                                .build());
        }

        /**
         * 6. GET List Skills
         * GET /api/v1/job/skill-list
         */
        @GetMapping("/skill-list")
        public ResponseEntity<ApiResultsResponse<String>> getSkillList() {
                List<String> data = jobService.getSkillList();
                return ResponseEntity.ok(ApiResultsResponse.<String>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy danh sách kỹ năng thành công!")
                                .results(data)
                                .build());
        }

        /**
         * 7. GET List Cities
         * GET /api/v1/job/city-list
         */
        @GetMapping("/city-list")
        public ResponseEntity<ApiResultsResponse<String>> getCityList() {
                List<String> data = jobService.getCityList();
                return ResponseEntity.ok(ApiResultsResponse.<String>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy danh sách thành phố thành công!")
                                .results(data)
                                .build());
        }

        /**
         * 8. GET Max Salary
         * GET /api/v1/job/max-salary
         */
        @GetMapping("/max-salary")
        public ResponseEntity<ApiDataResponse<BigDecimal>> getMaxSalary(@RequestParam("currency") String currency) {
                BigDecimal data = jobService.getMaxSalaryWithCurrency(currency);
                return ResponseEntity.ok(ApiDataResponse.<BigDecimal>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy mức lương cao nhất thành công!")
                                .data(data)
                                .build());
        }

        /**
         * 9. GET Related Jobs By ID
         * GET /api/v1/job/related-jobs/:id
         */
        @GetMapping("/related-jobs/{id}")
        public ResponseEntity<ApiResultsResponse<JobDetailDto>> getRelatedJobs(
                        @PathVariable Long id,
                        @Valid @Nullable RelatedJobQueryParameters queryParams) {
                List<JobDetailDto> data = jobService.getRelatedJobs(id, queryParams);
                return ResponseEntity.ok(ApiResultsResponse.<JobDetailDto>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy danh sách công việc liên quan thành công!")
                                .results(data)
                                .build());
        }

        /**
         * 10. GET List Job Dashboard of Company by ID Pageable
         * GET /api/v1/job/get-list/dashboard/company/:id
         */
        @GetMapping("/dashboard/company/{id}")
        public ResponseEntity<ApiPageResponse<JobDashboardDto>> getJobsForDashboard(
                        @PathVariable Long id,
                        @Nullable Pageable pageable,
                        @Nullable EmployerJobQueryDto params) {
                ApiPageResponse<JobDashboardDto> res = jobService.getJobsForDashboard(id, pageable, params);
                res.setStatusCode(HttpStatus.OK.value());
                res.setMessage("Get paginated list of dashboard successfully!");
                return ResponseEntity.ok(res);
        }

        /**
         * (Implied) POST NEW JOB
         * POST /api/v1/job
         */
        @PostMapping
        public ResponseEntity<ApiDataResponse<JobDetailDto>> createJob(
                        @Valid @RequestBody CreateJobDto createDto) {
                JobDetailDto data = jobService.createJob(createDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(ApiDataResponse.<JobDetailDto>builder()
                                .statusCode(HttpStatus.CREATED.value())
                                .message("Tạo công việc mới thành công!")
                                .data(data)
                                .build());
        }

        /**
         * (Implied) UPDATE JOB
         * PATCH /api/v1/job/:id
         */
        @PatchMapping("/{id}")
        public ResponseEntity<ApiDataResponse<JobDetailDto>> updatePartialJob(
                        @PathVariable Long id,
                        @Valid @RequestBody UpdateJobDto updateDto) {
                JobDetailDto data = jobService.updatePartialJob(id, updateDto);
                return ResponseEntity.ok(ApiDataResponse.<JobDetailDto>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Cập nhật công việc thành công!")
                                .data(data)
                                .build());
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiDataResponse<JobDetailDto>> updateJob(
                        @PathVariable Long id,
                        @Valid @RequestBody UpdateJobDto updateDto) {
                JobDetailDto data = jobService.updateJob(id, updateDto);
                return ResponseEntity.ok(ApiDataResponse.<JobDetailDto>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Cập nhật công việc thành công!")
                                .data(data)
                                .build());
        }

        /**
         * (Implied) SOFT DELETE JOB
         * DELETE /api/v1/job/:id
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiNoDataResponse> softDeleteJob(
                        @PathVariable Long id) {
                jobService.softDeleteJob(id);
                return ResponseEntity.ok(ApiNoDataResponse.builder()
                                .statusCode(200)
                                .message("Xóa mềm công việc thành công!")
                                .build());
        }
}
