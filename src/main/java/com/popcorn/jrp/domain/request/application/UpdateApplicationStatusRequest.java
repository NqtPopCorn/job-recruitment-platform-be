package com.popcorn.jrp.domain.request.application;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApplicationStatusRequest {
    @NotBlank(message = "Trạng thái không được để trống")
    private String status;
}
