package com.popcorn.jrp.domain.response;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateResponse {
    private String id;
    private String avatar;
    private String name;
    private String designation;
    private String location;
    private Double hourlyRate;
    private List<String> tags;
    private String category;
    private CandidateEntity.Gender gender;
    private String createdAt;
    private Boolean status;
//    private List<SocialMediaResponse> socialMedias;
}

