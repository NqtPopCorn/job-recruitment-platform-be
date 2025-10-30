package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.request.candidate.CreateCandidateDto;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateDto;
import com.popcorn.jrp.domain.response.candidate.CandidateDetailsResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateResponse;
import com.popcorn.jrp.domain.response.candidate.SoftDeleteCandidateResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CandidateMapper extends PageMapper {

    @Mapping(target = "tags", source = "skills")
    @Mapping(target = "category", source = "industry")
    CandidateResponse toResponse(CandidateEntity candidateEntity);

    @InheritConfiguration(name = "toResponse")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "socialMedias", source = "user.socialMedias")
    @Mapping(target = "qualification", source = "educationLevel")
    CandidateDetailsResponse toDetailsResponse(CandidateEntity candidateEntity);

    SoftDeleteCandidateResponse toSoftDeleteResponse(CandidateEntity candidateEntity);

    CandidateEntity createEntity(CreateCandidateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget CandidateEntity entity, UpdateCandidateDto dto);
}
