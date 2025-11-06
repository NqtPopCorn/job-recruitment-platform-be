package com.popcorn.jrp.domain.response.job;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // Kế thừa các trường từ JobDetailDto
public class JobDashboardDto extends JobDetailDto {

    // Thêm 2 trường đặc thù của Dashboard
    private Integer applications;
    private Boolean status;
}