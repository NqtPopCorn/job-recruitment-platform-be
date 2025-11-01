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
    private Integer datePosted;
    private Integer experience;
    private BigDecimal min;
    private BigDecimal max;
}