package com.popcorn.jrp.domain.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobQuery {
    private String search;
    private String location;
    private String category;
    private String type;
    private Integer datePosted;
    private String experience;
    private Double min;
    private Double max;
    private String currency;
}
