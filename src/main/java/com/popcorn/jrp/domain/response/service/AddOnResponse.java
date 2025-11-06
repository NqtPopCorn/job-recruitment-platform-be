package com.popcorn.jrp.domain.response.service;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddOnResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String type;
    private Integer quantity;
    private Integer durationDay;
    private Boolean isLifetime;
}