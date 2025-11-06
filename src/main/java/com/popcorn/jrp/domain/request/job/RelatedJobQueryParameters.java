package com.popcorn.jrp.domain.request.job;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RelatedJobQueryParameters {
    // Các tham số filter tùy chọn
    private String industry;
    private String country;
    private String city;
}
