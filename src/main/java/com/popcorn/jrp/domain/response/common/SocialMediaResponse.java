package com.popcorn.jrp.domain.response.common;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialMediaResponse {
    private String platform;
    private String url;
}
