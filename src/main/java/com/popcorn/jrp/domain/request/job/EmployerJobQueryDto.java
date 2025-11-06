package com.popcorn.jrp.domain.request.job;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EmployerJobQueryDto {

    private int page = 0;
    private int size = 10;

    private String category;
    private Integer datePosted; // số ngày đã đăng bài
}
