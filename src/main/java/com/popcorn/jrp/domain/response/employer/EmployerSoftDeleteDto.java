package com.popcorn.jrp.domain.response.employer;

import java.time.Instant;
import java.time.LocalDateTime;

import com.popcorn.jrp.domain.response.common.BaseResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployerSoftDeleteDto extends BaseResponse {
    private String id;
    private String name;
    private boolean status;
    private boolean isDeleted;
}
