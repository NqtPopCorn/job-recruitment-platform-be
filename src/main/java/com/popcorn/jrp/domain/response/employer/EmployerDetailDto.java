package com.popcorn.jrp.domain.response.employer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployerDetailDto {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String primaryIndustry;
    private String size;
    private int foundedIn;
    private String country;
    private String address;
    private String website;
    private boolean status;
}