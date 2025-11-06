package com.popcorn.jrp.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiNoDataResponse {
    private boolean success;
    private int statusCode;
    private String message;
}
