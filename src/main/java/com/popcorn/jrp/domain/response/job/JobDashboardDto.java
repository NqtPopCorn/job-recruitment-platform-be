package com.popcorn.jrp.domain.response.job;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // Kế thừa các trường từ JobDetailDto
@SuperBuilder
public class JobDashboardDto extends JobDetailDto {

}