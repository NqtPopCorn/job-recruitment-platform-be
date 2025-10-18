package com.popcorn.jrp.domain.response;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiPageResponse<T> {
    private int statusCode;
    private String message;
    private List<T> results;
    private Meta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Meta {
        private long totalItems;
        private int currentPage;
        private int pageSize;
        private int totalPages;
    }
}

