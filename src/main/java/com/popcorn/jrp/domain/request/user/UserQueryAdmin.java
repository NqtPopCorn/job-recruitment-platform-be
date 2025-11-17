package com.popcorn.jrp.domain.request.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQueryAdmin {
    private String search;
    private Boolean status;
}
