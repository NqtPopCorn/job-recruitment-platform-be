package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.job.*;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.job.JobDashboardDto;
import com.popcorn.jrp.domain.response.job.JobDetailDto;
import com.popcorn.jrp.domain.response.job.JobResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface service cho việc quản lý các Công việc (Jobs).
 */
public interface JobService {

    /**
     * 1. Lấy danh sách công việc có phân trang và lọc.
     * Tương ứng với: GET /api/v1/job
     *
     * @param queryParams Đối tượng chứa các tham số lọc.
     * @param pageable    Đối tượng phân trang.
     * @return ApiPageResponse chứa danh sách JobDetailDto và thông tin meta.
     */
    ApiPageResponse<JobResponseDto> getJobsPaginated(JobQueryParameters queryParams, Pageable pageable);

    /**
     * 3. Lấy chi tiết công việc bằng ID công việc.
     * Tương ứng với: GET /api/v1/job/detail/:id
     *
     * @param id ID của công việc (Kiểu Long cho Entity).
     * @return Chi tiết công việc.
     */
    JobDetailDto getJobById(Long id);

    /**
     * 4. Lấy danh sách các ngành nghề (industries) của công việc.
     * Tương ứng với: GET /api/v1/job/category-list
     *
     * @return Danh sách tên các ngành nghề.
     */
    List<String> getJobIndustryList();

    /**
     * 5. Lấy danh sách ngành nghề chính của một công ty.
     * Tương ứng với: GET /api/v1/job/category-list/company/:id
     *
     * @param companyId ID của công ty (Kiểu Long cho Entity).
     * @return Danh sách tên các ngành nghề của công ty đó.
     */
    List<String> getCompanyIndustryListByCompanyId(Long companyId);

    /**
     * 6. Lấy danh sách các kỹ năng (skills) từ các công việc.
     * Tương ứng với: GET /api/v1/job/skill-list
     *
     * @return Danh sách tên các kỹ năng.
     */
    List<String> getSkillList();

    /**
     * 7. Lấy danh sách các thành phố (cities) từ các công việc.
     * Tương ứng với: GET /api/v1/job/city-list
     *
     * @return Danh sách tên các thành phố.
     */
    List<String> getCityList();

    /**
     * 8. Lấy mức lương tối đa theo currency.
     * Tương ứng với: GET /api/v1/job/max-salary&currency=
     *
     * @return Mức lương cao nhất.
     */
    BigDecimal getMaxSalaryWithCurrency(String currency);

    /**
     * 9. Lấy danh sách công việc liên quan.
     * Tương ứng với: GET /api/v1/job/related-jobs/:id
     *
     * @param id          ID của công việc hiện tại (Kiểu Long).
     * @param queryParams Các tham số lọc tùy chọn (industry, country, city).
     * @return Danh sách các công việc liên quan.
     */
    List<JobDetailDto> getRelatedJobs(Long id, RelatedJobQueryParameters queryParams);

    /**
     * 10. Lấy danh sách công việc cho dashboard của công ty.
     * Tương ứng với: GET /api/v1/job/get-list/dashboard/company/:id
     *
     * @param companyId ID của công ty (Kiểu Long).
     * @return Danh sách JobDashboardDto (bao gồm status và số lượng applications).
     */
    ApiPageResponse<JobDashboardDto> getJobsForDashboard(Long companyId, Pageable pageable, EmployerJobQueryDto params);

    /**
     * (Implied) Tạo một công việc mới.
     * Tương ứng với: POST /api/v1/job
     *
     * @param createDto DTO chứa thông tin tạo mới.
     * @return Chi tiết công việc vừa tạo.
     */
    JobDetailDto createJob(CreateJobDto createDto);

    /**
     * (Implied) Cập nhật một công việc.
     * Tương ứng với: PATCH /api/v1/job/:id
     *
     * @param id        ID của công việc cần cập nhật (Kiểu Long).
     * @param updateDto DTO chứa thông tin cập nhật.
     * @return Chi tiết công việc sau khi cập nhật.
     */
    JobDetailDto updatePartialJob(Long id, UpdateJobDto updateDto);

    /**
     * (Implied) Cập nhật một công việc.
     * Tương ứng với: PATCH /api/v1/job/:id
     *
     * @param id        ID của công việc cần cập nhật (Kiểu Long).
     * @param updateDto DTO chứa thông tin cập nhật.
     * @return Chi tiết công việc sau khi cập nhật.
     */
    JobDetailDto updateJob(Long id, UpdateJobDto updateDto);

    /**
     * (Implied) Xóa mềm một công việc.
     * Tương ứng với: DELETE /api/v1/job/:id
     *
     * @param id ID của công việc cần xóa (Kiểu Long).
     */
    void softDeleteJob(Long id);
}