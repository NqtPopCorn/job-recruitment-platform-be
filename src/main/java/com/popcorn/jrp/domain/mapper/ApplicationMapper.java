package com.popcorn.jrp.domain.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.popcorn.jrp.domain.entity.ApplicationEntity;
import com.popcorn.jrp.domain.response.application.ApplicantResponseDto;
import com.popcorn.jrp.domain.response.application.ApplicationPageResponseDto;
import com.popcorn.jrp.domain.response.application.ApplicationResponseDto;
import com.popcorn.jrp.helper.JsonMapperHelper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false), uses = { JsonMapperHelper.class,
        JobMapper.class, CandidateMapper.class })
public interface ApplicationMapper {

    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "job.id", target = "jobId")
    ApplicationResponseDto toApplicationResponseDto(ApplicationEntity entity);

    ApplicationPageResponseDto toApplicationPageResponseDto(ApplicationEntity entity);

    ApplicantResponseDto toApplicantResponseDto(ApplicationEntity entity);
}
