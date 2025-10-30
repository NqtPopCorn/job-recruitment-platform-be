package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.request.employer.CreateEmployerDto;
import com.popcorn.jrp.domain.request.employer.UpdateEmployerDto;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.employer.*;
import org.mapstruct.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployerMapper extends PageMapper {

    // Dùng cho 1. GET PAGINATION
    @Mapping(target = "jobNumber", expression = "java(0)") // TODO: Cần logic riêng để đếm jobs
    EmployerPaginationDto toPaginationDto(EmployerEntity entity);

    // Dùng cho 2. GET LIST
    EmployerSimpleDto toSimpleDto(EmployerEntity entity);

    // Dùng cho 3, 4, 7, 8
    EmployerDetailDto toDetailDto(EmployerEntity entity);

    // Dùng cho 6. GET INDUSTRY LIST
    default IndustryLabelValueDto toIndustryLabelValueDto(String industry) {
        return new IndustryLabelValueDto(industry, industry);
    }
    List<IndustryLabelValueDto> toIndustryLabelValueDtoList(List<String> industries);

    // Dùng cho 7. POST NEW
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmployerEntity toEntity(CreateEmployerDto dto);

    // Dùng cho 8. PATCH
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UpdateEmployerDto dto, @MappingTarget EmployerEntity entity);

    // Dùng cho 9. SOFT DELETE
    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "localDateTimeToInstant")
    EmployerSoftDeleteDto toSoftDeleteDto(EmployerEntity entity);

    @Named("localDateTimeToInstant")
    default Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    // Mapper cho Job (Giả định)
    // RelatedJobDto toRelatedJobDto(JobEntity job);
    // List<RelatedJobDto> toRelatedJobDtoList(List<JobEntity> jobs);
}
