package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.employer.EmployerQueryAdmin;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.employer.EmployerListAdminResponse;
import com.popcorn.jrp.service.EmployerAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import jakarta.annotation.Nullable;

@RestController
@RequestMapping("/api/v1/admin/companies")
@RequiredArgsConstructor
public class EmployerAdminController {

    private final EmployerAdminService employerAdminService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiPageResponse<EmployerListAdminResponse> getEmployerListAdmin(
            @Nullable Pageable pageable,
            @Nullable EmployerQueryAdmin request) {

        var response = employerAdminService.getEmployerListAdmin(request, pageable);
        response.setMessage("Lấy danh sách công ty thành công!");
        response.setStatusCode(HttpStatus.OK.value());
        return response;
    }

    @PatchMapping("/{id}/lock")
    @ResponseStatus(HttpStatus.OK)
    public ApiNoDataResponse lockEmployer(@PathVariable Long id) {
        return employerAdminService.lockEmployer(id);
    }
}
