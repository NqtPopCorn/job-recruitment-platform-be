package com.popcorn.jrp.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.jrp.domain.response.job.JobTypeDto;
import com.popcorn.jrp.domain.response.job.SalaryDto;
import com.popcorn.jrp.domain.response.job.SocialMediaDto;
import com.popcorn.jrp.domain.response.job.WorkTimeDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Lớp helper để giúp MapStruct xử lý chuyển đổi JSON String.
 */
@Component
public class JsonMapperHelper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // --- Object to JSON String ---

    @org.mapstruct.Named("objectToJsonString")
    public String objectToJsonString(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // Log lỗi ở đây
            return null;
        }
    }

    // --- JSON String to Object ---

    @org.mapstruct.Named("jsonToListString")
    public List<String> jsonToListString(String json) {
        if (json == null || json.isEmpty()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    @org.mapstruct.Named("jsonToJobTypeList")
    public List<JobTypeDto> jsonToJobTypeList(String json) {
        if (json == null || json.isEmpty()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<JobTypeDto>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    @org.mapstruct.Named("jsonToSocialMediaList")
    public List<SocialMediaDto> jsonToSocialMediaList(String json) {
        if (json == null || json.isEmpty()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<SocialMediaDto>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    @org.mapstruct.Named("jsonToSalaryDto")
    public SalaryDto jsonToSalaryDto(String json) {
        if (json == null || json.isEmpty()) return null;
        try {
            return objectMapper.readValue(json, SalaryDto.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @org.mapstruct.Named("jsonToWorkTimeDto")
    public WorkTimeDto jsonToWorkTimeDto(String json) {
        if (json == null || json.isEmpty()) return null;
        try {
            return objectMapper.readValue(json, WorkTimeDto.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}