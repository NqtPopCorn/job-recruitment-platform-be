package com.popcorn.jrp.domain.response.job;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SocialMediaDto {
    private String platform;
    private String url;
}