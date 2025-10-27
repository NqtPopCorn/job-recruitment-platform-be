package com.popcorn.jrp.domain.request.employer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateEmployerDto {

    private String name;

    private String phone;

    private String website;

    // private String primaryIndustry;
    // private String size;
    // private Integer foundedIn;
    // private String description;
    // ...
}
