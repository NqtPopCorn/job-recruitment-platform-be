package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.job.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.job.JobDashboardDto;
import com.popcorn.jrp.domain.response.job.JobDetailDto;
import com.popcorn.jrp.service.JobService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        public @ResponseBody ApiPageResponse<JobDetailDto> getJobsPaginated(
                        @Nullable Pageable pageable,
                        @Nullable JobQueryParameters queryParams) {
                ApiPageResponse<JobDetailDto> body = jobService.getJobsPaginated(queryParams, pageable);

                // Gán message và status code theo chuẩn
                body.setMessage("Lấy danh sách công việc phân trang thành công!");
                body.setStatusCode(200);
                return body;
        }

        /**
         * 2. GET LIST JOB FOR CLIENT (Non-paginated)
         * GET /api/v1/job/get-list
         */
        @GetMapping("/get-list")
        public ResponseEntity<ApiDataResponse<List<JobDetailDto>>> getAllJobs() {
                List<JobDetailDto> data = jobService.getAllJobs();
                return ResponseEntity.ok(ApiDataResponse.<List<JobDetailDto>>builder()
                                .data(data)
                                .message("Lấy danh sách công việc thành công!")
                                .statusCode(200)
                                .build());
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
                                .data(data)
                                .message("Lấy thông tin chi tiết công việc thành công!")
                                .statusCode(200)
                                .build());
        }

        /**
         * 4. GET List Category (Industry of Job)
         * GET /api/v1/job/category-list
         */
        @GetMapping("/category-list")
        public ResponseEntity<ApiDataResponse<List<String>>> getJobIndustryList() {
                List<String> data = jobService.getJobIndustryList();
                return ResponseEntity.ok(ApiDataResponse.<List<String>>builder()
                                .data(data)
                                .message("Lấy danh sách danh mục thành công!")
                                .statusCode(200)
                                .build());
        }

        /**
         * 5. GET List Primary Industry of Company
         * GET /api/v1/job/category-list/company/:id
         */
        @GetMapping("/category-list/company/{id}")
        public ResponseEntity<ApiDataResponse<List<String>>> getCompanyIndustryList(
                        @PathVariable Long id) {
                List<String> data = jobService.getCompanyIndustryList(id);
                return ResponseEntity.ok(ApiDataResponse.<List<String>>builder()
                                .data(data)
                                .message("Lấy danh sách danh mục công ty thành công!")
                                .statusCode(200)
                                .build());
        }

        /**
         * 6. GET List Skills
         * GET /api/v1/job/skill-list
         */
        @GetMapping("/skill-list")
        public ResponseEntity<ApiDataResponse<List<String>>> getSkillList() {
                List<String> data = jobService.getSkillList();
                return ResponseEntity.ok(ApiDataResponse.<List<String>>builder()
                                .data(data)
                                .message("Lấy danh sách kỹ năng thành công!")
                                .statusCode(200)
                                .build());
        }

        /**
         * 7. GET List Cities
         * GET /api/v1/job/city-list
         */
        @GetMapping("/city-list")
        public ResponseEntity<ApiDataResponse<List<String>>> getCityList() {
                List<String> data = jobService.getCityList();
                return ResponseEntity.ok(ApiDataResponse.<List<String>>builder()
                                .data(data)
                                .message("Lấy danh sách thành phố thành công!")
                                .statusCode(200)
                                .build());
        }

        /**
         * 8. GET Max Salary
         * GET /api/v1/job/max-salary
         */
        @GetMapping("/max-salary")
        public ResponseEntity<ApiDataResponse<BigDecimal>> getMaxSalary(@RequestParam("currency") String currency) {
                var data = jobService.getMaxSalaryWithCurrency(currency);
                return ResponseEntity.ok(ApiDataResponse.<BigDecimal>builder()
                                .data(data)
                                .message("Lấy mức lương cao nhất thành công!")
                                .statusCode(200)
                                .build());
        }

        /**
         * 9. GET Related Jobs By ID
         * GET /api/v1/job/related-jobs/:id
         */
        @GetMapping("/related-jobs/{id}")
        public ResponseEntity<ApiDataResponse<List<JobDetailDto>>> getRelatedJobs(
                        @PathVariable Long id,
                        @Valid @Nullable RelatedJobQueryParameters queryParams) {
                List<JobDetailDto> data = jobService.getRelatedJobs(id, queryParams);
                return ResponseEntity.ok(ApiDataResponse.<List<JobDetailDto>>builder()
                                .data(data)
                                .message("Lấy danh sách công việc liên quan thành công!")
                                .statusCode(200)
                                .build());
        }

        /**
         * 10. GET List Job Dashboard of Company by ID Pageable
         * GET /api/v1/job/get-list/dashboard/company/:id
         */
        @GetMapping("/get-list/dashboard/company/{id}")
        public ResponseEntity<ApiPageResponse<JobDashboardDto>> getJobsForDashboard(
                        @PathVariable Long id,
                        @RequestBody EmployerJobQueryDto params) {
                var res = jobService.getJobsForDashboard(id, params);
                res.setStatusCode(200);
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
                                .data(data)
                                .message("Tạo công việc mới thành công!")
                                .statusCode(HttpStatus.CREATED.value())
                                .build());
        }

        /**
         * (Implied) UPDATE JOB
         * PATCH /api/v1/job/:id
         */
        @PatchMapping("/{id}")
        public ResponseEntity<ApiDataResponse<JobDetailDto>> updateJob(
                        @PathVariable Long id,
                        @Valid @RequestBody UpdateJobDto updateDto) {
                JobDetailDto data = jobService.updateJob(id, updateDto);
                return ResponseEntity.ok(ApiDataResponse.<JobDetailDto>builder()
                                .data(data)
                                .message("Cập nhật công việc thành công!")
                                .statusCode(200)
                                .build());
        }

        /**
         * (Implied) SOFT DELETE JOB
         * DELETE /api/v1/job/:id
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiDataResponse<Object>> softDeleteJob(
                        @PathVariable Long id) {
                jobService.softDeleteJob(id);
                return ResponseEntity.ok(ApiDataResponse.builder()
                                .data(null)
                                .message("Xóa mềm công việc thành công!")
                                .statusCode(200)
                                .build());
        }
}
