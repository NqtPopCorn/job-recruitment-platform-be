package com.popcorn.jrp.domain.request.job;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho thời gian làm việc.
 * Các trường là tùy chọn.
 */
@Data
@NoArgsConstructor
public class JobWorkTimeDto {
    private String from; // "08:00"
    private String to;   // "17:00"
}
