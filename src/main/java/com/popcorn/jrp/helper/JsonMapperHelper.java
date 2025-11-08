package com.popcorn.jrp.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.response.job.JobTypeDto;
import com.popcorn.jrp.domain.response.job.SalaryDto;
import com.popcorn.jrp.domain.response.job.SocialMediaDto;
import com.popcorn.jrp.domain.response.job.WorkTimeDto;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lớp helper để giúp MapStruct xử lý chuyển đổi JSON String.
 */
@Component
public class JsonMapperHelper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // --- Object to JSON String ---

    @Named("objectToJsonString")
    public String objectToJsonString(Object obj) {
        if (obj == null)
            return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // Log lỗi ở đây
            return null;
        }
    }

    // --- JSON String to Object ---

    @Named("jsonToListString")
    public List<String> jsonToListString(String json) {
        if (json == null || json.isEmpty())
            return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {
            });
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    @Named("jsonToJobTypeList")
    public List<JobTypeDto> jsonToJobTypeList(String json) {
        if (json == null || json.isEmpty())
            return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<JobTypeDto>>() {
            });
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    @Named("jsonToSocialMediaList")
    public List<SocialMediaDto> jsonToSocialMediaList(String json) {
        if (json == null || json.isEmpty())
            return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<SocialMediaDto>>() {
            });
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    // @Named("parseSalary")
    // public SalaryDto parseSalary(CandidateEntity candidate) {
    // if (candidate == null ) return null;
    // return
    // }

    @Named("jsonToWorkTimeDto")
    public WorkTimeDto jsonToWorkTimeDto(String json) {
        if (json == null || json.isEmpty())
            return null;
        try {
            return objectMapper.readValue(json, WorkTimeDto.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}