package com.popcorn.jrp.domain.response.employer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class    EmployerPaginationDto {
    private String id;
    private String email;
    private String name;
    private String primaryIndustry;
    private String size;
    private int foundedIn;
    private String description;
    private String phone;
    private String address;
    private int jobNumber;
    private String logo;
    private boolean status;
    // private List<String> socialMedias;
}
