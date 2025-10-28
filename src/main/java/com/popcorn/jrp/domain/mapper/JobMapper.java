package com.popcorn.jrp.domain.mapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.domain.entity.JobTypeEntity;
import com.popcorn.jrp.domain.entity.SkillEntity;
import com.popcorn.jrp.domain.response.job.JobResponse;
import com.popcorn.jrp.domain.response.job.SalaryResponse;
import com.popcorn.jrp.domain.response.job.WorkTimeResponse;

@Mapper(componentModel = "spring")
public interface JobMapper extends PageMapper<JobResponse> {

    @Mapping(target = "jobTitle", source = "name")
    @Mapping(target = "responsibilities", expression = "java(convertJsonToList(jobEntity.getResponsibilities()))")
    @Mapping(target = "skillAndExperience", expression = "java(convertJsonToList(jobEntity.getSkillAndExperiences()))")
    @Mapping(target = "salary", expression = "java(toSalaryResponse(jobEntity))")
    @Mapping(target = "workTime", expression = "java(toWorkTimeResponse(jobEntity))")
    @Mapping(target = "jobType", expression = "java(toJobTypeList(jobEntity.getJobTypes()))")
    @Mapping(target = "skills", expression = "java(toSkillList(jobEntity.getSkills()))")
    @Mapping(target = "datePosted", expression = "java(toLocalDate(jobEntity.getCreatedAt()))")
    @Mapping(target = "expireDate", source = "expirationDate")
    JobResponse toResponse(JobEntity jobEntity);

    // ✅ Thêm default method để hỗ trợ Page mapping
    default Page<JobResponse> toPageResponse(Page<JobEntity> entities) {
        return entities.map(entity -> toResponse(entity));
    }

    // ✅ Hàm hỗ trợ
    default List<String> convertJsonToList(String json) {
        if (json == null || json.isEmpty())
            return Collections.emptyList();
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<String>>() {
            });
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // 🔹 Map JobTypeEntity → List<String>
    default List<String> toJobTypeList(List<JobTypeEntity> jobTypes) {
        if (jobTypes == null)
            return Collections.emptyList();
        return jobTypes.stream()
                .map(JobTypeEntity::getName)
                .collect(Collectors.toList());
    }

    // 🔹 Map SkillEntity → List<String>
    default List<String> toSkillList(List<SkillEntity> skills) {
        if (skills == null)
            return Collections.emptyList();
        return skills.stream()
                .map(SkillEntity::getName)
                .collect(Collectors.toList());
    }

    // 🔹 Combine salary fields
    default SalaryResponse toSalaryResponse(JobEntity entity) {
        if (entity.getMinSalary() == null && entity.getMaxSalary() == null)
            return null;
        return SalaryResponse.builder()
                .min(entity.getMinSalary())
                .max(entity.getMaxSalary())
                .currency(entity.getCurrency())
                .unit(entity.getUnit())
                .negotiable(entity.getNegotiable())
                .build();
    }

    // 🔹 Combine workTimeFrom / workTimeTo
    default WorkTimeResponse toWorkTimeResponse(JobEntity entity) {
        if (entity.getWorkTimeFrom() == null && entity.getWorkTimeTo() == null)
            return null;
        return WorkTimeResponse.builder()
                .from(entity.getWorkTimeFrom())
                .to(entity.getWorkTimeTo())
                .build();
    }

    // 🔹 Convert LocalDateTime → LocalDate
    default LocalDate toLocalDate(java.time.LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }
}
