package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.employer.EmployerQueryAdmin;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.employer.EmployerListAdminResponse;
import org.springframework.data.domain.Pageable;

public interface EmployerAdminService {
    ApiPageResponse<EmployerListAdminResponse> getEmployerListAdmin(EmployerQueryAdmin request, Pageable pageable);
    ApiNoDataResponse lockEmployer(Long id);
}
