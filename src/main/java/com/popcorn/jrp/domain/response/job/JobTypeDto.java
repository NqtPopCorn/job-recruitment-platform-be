package com.popcorn.jrp.domain.response.job;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho loại công việc (Full-time, Part-time, etc.)
 */
@Data
@NoArgsConstructor
public class JobTypeDto {

    @NotBlank(message = "styleClass không được để trống")
    private String styleClass;

    @NotBlank(message = "type không được để trống")
    private String type;
}