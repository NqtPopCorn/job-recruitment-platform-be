package com.popcorn.jrp.domain.response.employer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployerSimpleDto {
    private String id;
    private String email;
    private String name;
    private String primaryIndustry;
    private String size;
    private int foundedIn;
    private String country;
    private String city;
    private String address;
    private String website;
    private String logo;
    private boolean status;
}
