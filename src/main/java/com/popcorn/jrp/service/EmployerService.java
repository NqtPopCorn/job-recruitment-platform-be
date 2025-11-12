package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.employer.CreateEmployerDto;
import com.popcorn.jrp.domain.request.employer.EmployerQueryParameters;
import com.popcorn.jrp.domain.request.employer.UpdateEmployerDto;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.common.IndustryLabelValueDto;
import com.popcorn.jrp.domain.response.employer.*;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Interface service cho việc quản lý Nhà tuyển dụng (Employer/Company).
 */
public interface EmployerService {

    /**
     * Lấy danh sách nhà tuyển dụng có phân trang và lọc.
     * Tương ứng với: GET /api/v1/company
     *
     * @param queryParams Đối tượng chứa các tham số lọc (search, location,
     *                    industry...).
     * @param pageable    Đối tượng chứa thông tin phân trang (page, size, sort).
     * @return Một trang (Page) chứa danh sách EmployerPaginationDto.
     */
    ApiPageResponse<EmployerPaginationDto> getEmployersPaginated(EmployerQueryParameters queryParams,
            Pageable pageable);

    /**
     * Lấy danh sách tất cả nhà tuyển dụng (không phân trang).
     * Tương ứng với: GET /api/v1/company/get-list
     *
     * @return Danh sách EmployerSimpleDto.
     */
    // List<EmployerSimpleDto> getAllEmployers();

    /**
     * Lấy thông tin chi tiết nhà tuyển dụng bằng ID của nhà tuyển dụng.
     * Tương ứng với: GET /api/v1/company/details/:id
     *
     * @param id ID của nhà tuyển dụng.
     * @return Chi tiết nhà tuyển dụng.
     */
    EmployerDetailDto getEmployerDetailsById(Long id);

    /**
     * Lấy thông tin chi tiết nhà tuyển dụng bằng ID của người dùng (User).
     * Tương ứng với: GET /api/v1/company/details/user/:id
     *
     * @param userId ID của người dùng.
     * @return Chi tiết nhà tuyển dụng.
     */
    EmployerDetailDto getEmployerDetailsByUserId(Long userId);

    /**
     * Lấy danh sách các công việc liên quan (đã đăng) bởi nhà tuyển dụng.
     * Tương ứng với: GET /api/v1/company/related-jobs/:companyId
     *
     * @param employerId ID của nhà tuyển dụng.
     * @return Danh sách các công việc liên quan.
     */
    List<RelatedJobDto> getRelatedJobsByEmployerId(Long employerId);

    /**
     * Lấy danh sách các ngành nghề (industries) của các nhà tuyển dụng.
     * Tương ứng với: GET /api/v1/company/industry-list
     *
     * @return Danh sách các ngành nghề theo định dạng {label, value}.
     */
    List<IndustryLabelValueDto> getIndustryList();

    /**
     * Tạo một nhà tuyển dụng mới.
     * Tương ứng với: POST /api/v1/company
     *
     * @param createDto DTO chứa thông tin để tạo mới.
     * @return Chi tiết nhà tuyển dụng vừa được tạo.
     */
    // EmployerDetailDto createEmployer(CreateEmployerDto createDto);

    /**
     * Cập nhật thông tin của một nhà tuyển dụng.
     * Tương ứng với: PATCH /api/v1/company/:id
     *
     * @param id        ID của nhà tuyển dụng cần cập nhật.
     * @param updateDto DTO chứa các thông tin cần cập nhật.
     * @return Chi tiết nhà tuyển dụng sau khi đã cập nhật.
     */
    EmployerDetailDto updateEmployer(Long id, UpdateEmployerDto updateDto);

    /**
     * Xóa mềm (soft delete) một nhà tuyển dụng.
     * Tương ứng với: DELETE /api/v1/company/:id
     *
     * @param id ID của nhà tuyển dụng cần xóa mềm.
     * @return DTO chứa thông tin sau khi xóa mềm (ID, status, updatedAt).
     */
    EmployerSoftDeleteDto softDeleteEmployer(Long id);

    /**
     * Add potential candidate.
     */
    boolean togglePotentialCandidate(Long employerId, Long potentialCandidateId);

    /**
     * Check potential candidate.
     */
    boolean checkPotentialCandidate(Long employerId, Long potentialCandidateId);
}
