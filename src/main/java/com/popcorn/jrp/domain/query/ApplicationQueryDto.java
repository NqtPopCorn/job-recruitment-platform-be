package com.popcorn.jrp.domain.query;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationQueryDto {
    private Long candidateId;
    private String status;
    private Integer datePosted = 0;
}
