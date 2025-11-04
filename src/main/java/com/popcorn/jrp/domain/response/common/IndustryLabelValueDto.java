package com.popcorn.jrp.domain.response.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndustryLabelValueDto {
    private String label;
    private String value;
}
