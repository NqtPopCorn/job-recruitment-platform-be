package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.response.ApiPageResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

// @Mapper(componentModel = "spring")
public interface PageMapper<T> {

    default ApiPageResponse<T> toApiPageResponse(Page<T> page) {
        return ApiPageResponse.<T>builder()
                .results(page.getContent())
                .meta(ApiPageResponse.Meta.builder()
                        .totalPages(page.getTotalPages())
                        .pageSize(page.getSize())
                        .currentPage(page.getNumber())
                        .totalItems(page.getTotalElements())
                        .build())
                .build();
    }
}
