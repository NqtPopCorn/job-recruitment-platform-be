package com.popcorn.jrp.domain.response.job;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkTimeDto {
    private String from; // Ví dụ: "09:00"
    private String to;   // Ví dụ: "18:00"
}