package com.popcorn.jrp.domain.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.request.employer.UpdateEmployerDto;
import com.popcorn.jrp.domain.response.common.IndustryLabelValueDto;
import com.popcorn.jrp.domain.response.common.SocialMediaDto;
import com.popcorn.jrp.domain.response.employer.*;
import org.mapstruct.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployerMapper extends PageMapper {

    // Dùng cho 1. GET PAGINATION
    @Mapping(target = "jobNumber", expression = "java(0)") // TODO: Cần logic riêng để đếm jobs
    EmployerPaginationDto toPaginationDto(EmployerEntity entity);

    // Dùng cho 2. GET LIST
    EmployerSimpleDto toSimpleDto(EmployerEntity entity);

    // Dùng cho 3, 4, 7, 8
    @Mapping(target = "socialMedias", expression = "java(mapStringToSocialMedias(entity.getSocialMedias()))")
    @Mapping(target = "userId", source = "user.id")
    EmployerDetailDto toDetailDto(EmployerEntity entity);

    // Dùng cho 6. GET INDUSTRY LIST
    default IndustryLabelValueDto toIndustryLabelValueDto(String industry) {
        return new IndustryLabelValueDto(industry, industry);
    }

    List<IndustryLabelValueDto> toIndustryLabelValueDtoList(List<String> industries);

    // Dùng cho 8. PATCH
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // @Mapping(target = "socialMedias", expression =
    // "java(mapSocialMediasToString(dto.getSocialMedias()))")
    void updateEntityFromDto(UpdateEmployerDto dto, @MappingTarget EmployerEntity entity);

    // Dùng cho 9. SOFT DELETE
    // @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName =
    // "localDateTimeToInstant")
    // @Mapping(source = "deletedAt", target = "deletedAt", qualifiedByName =
    // "localDateTimeToInstant")
    EmployerSoftDeleteDto toSoftDeleteDto(EmployerEntity entity);

    @Named("localDateTimeToInstant")
    default Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return null;
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

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

    // Mapper cho Job (Giả định)
    // RelatedJobDto toRelatedJobDto(JobEntity job);
    // List<RelatedJobDto> toRelatedJobDtoList(List<JobEntity> jobs);
    @Mapping(target = "jobNumber", expression = "java(0)")
    EmployerListAdminResponse toListAdminResponse(EmployerEntity entity);
}
