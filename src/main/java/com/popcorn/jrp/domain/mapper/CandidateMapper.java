package com.popcorn.jrp.domain.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.request.candidate.CreateCandidateDto;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateDto;
import com.popcorn.jrp.domain.response.candidate.CandidateDetailsResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateResponse;
import com.popcorn.jrp.domain.response.candidate.SoftDeleteCandidateResponse;
import com.popcorn.jrp.domain.response.common.SocialMediaDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false))
public interface CandidateMapper extends PageMapper {

    @Mapping(target = "tags", source = "skills")
    @Mapping(target = "category", source = "industry")
    CandidateResponse toResponse(CandidateEntity candidateEntity);

    @InheritConfiguration(name = "toResponse")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "socialMedias", expression = "java(mapStringToSocialMedias(candidateEntity.getSocialMedias()))")
    @Mapping(target = "qualification", source = "educationLevel")
    CandidateDetailsResponse toDetailsResponse(CandidateEntity candidateEntity);

    SoftDeleteCandidateResponse toSoftDeleteResponse(CandidateEntity candidateEntity);

    // CandidateEntity createEntity(CreateCandidateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget CandidateEntity entity, UpdateCandidateDto dto);

    // ----- Custom mapping methods -----
    default String mapSocialMediasToString(List<SocialMediaDto> socialMedias) {
        if (socialMedias == null)
            return null;
        try {
            return new ObjectMapper().writeValueAsString(socialMedias);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting socialMedias to JSON", e);
        }
    }

    default List<SocialMediaDto> mapStringToSocialMedias(String socialMedias) {
        if (socialMedias == null || socialMedias.isEmpty())
            return Collections.emptyList();
        try {
            return new ObjectMapper().readValue(socialMedias, new TypeReference<List<SocialMediaDto>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON to socialMedias", e);
        }
    }
}
