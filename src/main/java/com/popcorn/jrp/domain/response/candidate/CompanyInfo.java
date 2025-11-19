package com.popcorn.jrp.domain.response.candidate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyInfo {
    Long employerId;
    String name;
    String logo;
}