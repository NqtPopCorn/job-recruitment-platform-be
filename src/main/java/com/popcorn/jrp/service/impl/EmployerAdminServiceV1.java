package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.mapper.EmployerMapper;
import com.popcorn.jrp.domain.request.employer.EmployerQueryAdmin;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.employer.EmployerListAdminResponse;
import com.popcorn.jrp.exception.BadRequestException;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.EmployerAdminRepository;
import com.popcorn.jrp.repository.spec.EmployerAdminSpecification;
import com.popcorn.jrp.service.EmployerAdminService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class EmployerAdminServiceV1 implements EmployerAdminService {

    EmployerAdminRepository employerAdminRepository;
    EmployerMapper mapper;
    EmployerAdminSpecification employerAdminSpecification;

    @Override
    public ApiPageResponse<EmployerListAdminResponse> getEmployerListAdmin(EmployerQueryAdmin request, Pageable pageable) {
        try {
            Specification<EmployerEntity> spec = employerAdminSpecification.getAdminSpecification(request);
            var page = employerAdminRepository.findAll(spec, pageable);
            return mapper.toApiPageResponse(page.map(mapper::toListAdminResponse));
        } catch (Exception e) {
            throw new BadRequestException("Page request error: " + e.getMessage());
        }
    }

    @Override
    public ApiNoDataResponse lockEmployer(Long id) {
        EmployerEntity empl = employerAdminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy công ty với ID: " + id));

        empl.setStatus(false);
        employerAdminRepository.save(empl);

        return ApiNoDataResponse.builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Khóa hồ sơ công ty thành công!")
                .build();
    }
}
