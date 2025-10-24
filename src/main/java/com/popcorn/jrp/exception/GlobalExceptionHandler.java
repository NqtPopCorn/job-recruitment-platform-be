package com.popcorn.jrp.exception;

import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateDetailsResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiDataResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(404)
                .body(ApiDataResponse.builder()
                    .statusCode(404)
                    .message(e.getMessage())
                    .build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiDataResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(400)
                .body(ApiDataResponse.builder()
                        .statusCode(400)
                        .message(e.getMessage())
                        .build());
    }

    // handle Validate exception
}
