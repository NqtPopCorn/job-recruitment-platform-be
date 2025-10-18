package com.popcorn.jrp.domain.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiDataResponse<T> {
    private int statusCode;
    private String message;
    private T data;
}
