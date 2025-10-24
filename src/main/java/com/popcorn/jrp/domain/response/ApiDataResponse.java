package com.popcorn.jrp.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiDataResponse<T> {
    private int statusCode;
    private String message;
    private T data;
}
