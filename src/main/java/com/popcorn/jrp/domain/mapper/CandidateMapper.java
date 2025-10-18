package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.response.CandidateResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    CandidateResponse toResponse(CandidateEntity candidateEntity);
}
