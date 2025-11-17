package com.popcorn.jrp.domain.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.jrp.domain.entity.ApplicationEntity;
import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateDto;
import com.popcorn.jrp.domain.response.candidate.CandidateDetailsAdminResponse;
import com.popcorn.jrp.domain.response.candidate.*;
import com.popcorn.jrp.domain.response.common.SocialMediaDto;

import java.util.Collections;
import java.util.List;

import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false))
public interface CandidateMapper extends PageMapper {

    CandidateResponse toResponse(CandidateEntity candidateEntity);

    @InheritConfiguration(name = "toResponse")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "socialMedias", expression = "java(mapStringToSocialMedias(candidateEntity.getSocialMedias()))")
    @Mapping(target = "qualification", source = "educationLevel")
    CandidateDetailsResponse toDetailsResponse(CandidateEntity candidateEntity);

    @Mapping(target = "tags", source = "skills")
    @Mapping(target = "category", source = "industry")
    CandidateListAdminResponse toListAdminResponse(CandidateEntity candidateEntity);

    @Mapping(target = "tags", source = "skills")
    @Mapping(target = "category", source = "industry")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "socialMedias", expression = "java(mapStringToSocialMedias(candidateEntity.getSocialMedias()))")
    @Mapping(target = "qualification", source = "educationLevel")
    @Mapping(target = "currentSalary", expression = "java(formatSalary(candidateEntity.getCurrentSalary(), candidateEntity.getCurrency()))")
    @Mapping(target = "expectedSalary", expression = "java(formatSalary(candidateEntity.getExpectedSalary(), candidateEntity.getCurrency()))")
    CandidateDetailsAdminResponse toDetailsAdminResponse(CandidateEntity candidateEntity);

    SoftDeleteCandidateResponse toSoftDeleteResponse(CandidateEntity candidateEntity);

    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "applicationId", source = "id")
    @Mapping(target = "jobTitle", source = "job.title")
    @Mapping(target = "companyName", source = "job.employer.name")
    @Mapping(target = "companyLogo", source = "job.employer.logo")
    @Mapping(target = "location", source = "job.location")
    @Mapping(target = "city", source = "job.city")
    @Mapping(target = "country", source = "job.country")
    @Mapping(target = "minSalary", source = "job.minSalary")
    @Mapping(target = "maxSalary", source = "job.maxSalary")
    @Mapping(target = "currency", source = "job.currency")
    @Mapping(target = "appliedAt", source = "appliedAt")
    @Mapping(target = "applicationStatus", expression = "java(application.getStatus() != null ? application.getStatus().name() : \"PENDING\")")
    @Mapping(target = "jobTypes", source = "job.jobTypes")
    @Mapping(target = "isUrgent", expression = "java(application.getJob() != null && application.getJob().getExpirationDate() != null && application.getJob().getExpirationDate().isBefore(java.time.LocalDateTime.now().plusDays(3)))")
    @Mapping(target = "level", source = "job.level")
    JobAppliedRecentlyResponse toJobAppliedRecentlyResponse(ApplicationEntity application);

    // Map list of applications
    List<JobAppliedRecentlyResponse> toJobAppliedRecentlyResponseList(List<ApplicationEntity> applications);
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

    default String formatSalary(java.math.BigDecimal salary, String currency) {
        if (salary == null) return "0 VND";
        return String.format("%,d %s", salary.longValue(), currency != null ? currency : "VND");
    }
}
