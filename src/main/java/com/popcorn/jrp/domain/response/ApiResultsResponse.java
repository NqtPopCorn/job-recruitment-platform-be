package com.popcorn.jrp.domain.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResultsResponse<T> {
    private int statusCode;
    private String message;
    private List<T> results;
}
