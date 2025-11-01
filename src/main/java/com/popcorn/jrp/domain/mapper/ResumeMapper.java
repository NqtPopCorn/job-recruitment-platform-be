package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.ResumeEntity;
import com.popcorn.jrp.domain.request.candidate.UpdateResumeDto;
import com.popcorn.jrp.domain.response.candidate.ResumeResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ResumeMapper {

    @Mapping(target = "candidateId", source = "candidate.id")
    @Mapping(target = "url", source = "fileName", qualifiedByName = "buildUrl")
    ResumeResponseDto toResponse(ResumeEntity resumeEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget ResumeEntity resumeEntity, UpdateResumeDto resumeResponseDto);

    @Named("buildUrl")
    default String buildFileUrl(String fileName) {
        return "/resumes/" + fileName;
    }
}
