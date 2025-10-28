package com.popcorn.jrp.domain.request.job;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class JobQueryParameters {
    // Các tham số filter từ Query Params
    private String search;
    private String location;
    private String category;
    private String type;
    private String datePosted;
    private String experience;
    private Double min; // Hoặc BigDecimal nếu cần độ chính xác cao
    private Double max;
}