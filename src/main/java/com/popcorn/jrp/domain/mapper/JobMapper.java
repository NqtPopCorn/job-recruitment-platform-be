package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.helper.JsonMapperHelper;
import com.popcorn.jrp.domain.request.job.CreateJobDto;
import com.popcorn.jrp.domain.request.job.UpdateJobDto;
import com.popcorn.jrp.domain.response.job.CompanyInJobDto;
import com.popcorn.jrp.domain.response.job.JobDashboardDto;
import com.popcorn.jrp.domain.response.job.JobDetailDto;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", uses = {JsonMapperHelper.class})
public interface JobMapper extends PageMapper {

    // Định dạng ngày theo "d/M/yyyy" (ví dụ: 6/8/2025)
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");

    // --- Entity to DTO ---

    @Mapping(source = "id", target = "id", qualifiedByName = "longToString")
    @Mapping(source = "employer.logo", target = "logo") // Lấy logo từ Employer
    @Mapping(source = "employer", target = "company") // Map lồng ghép
    @Mapping(source = "responsibilities", target = "responsibilities", qualifiedByName = "jsonToListString")
    @Mapping(source = "skillAndExperiences", target = "skillAndExperience", qualifiedByName = "jsonToListString")
    @Mapping(source = "jobType", target = "jobTypes")
    @Mapping(source = "expirationDate", target = "expireDate", qualifiedByName = "localDateToDateString")
    // Giả định bạn đã thêm 'salary' và 'workTime' (dạng String JSON) vào JobEntity
    @Mapping(source = "salary", target = "salary", qualifiedByName = "jsonToSalaryDto")
    @Mapping(source = "workTime", target = "workTime", qualifiedByName = "jsonToWorkTimeDto")
    JobDetailDto toDetailDto(JobEntity entity);

    @Mapping(source = "id", target = "id", qualifiedByName = "longToString")
    @Mapping(source = "employer.logo", target = "logo")
    @Mapping(source = "employer", target = "company")
    @Mapping(source = "responsibilities", target = "responsibilities", qualifiedByName = "jsonToListString")
    @Mapping(source = "skillAndExperiences", target = "skillAndExperience", qualifiedByName = "jsonToListString")
    @Mapping(source = "jobType", target = "jobTypes")
    @Mapping(source = "expirationDate", target = "expireDate", qualifiedByName = "localDateToDateString")
    @Mapping(source = "salary", target = "salary", qualifiedByName = "jsonToSalaryDto")
    @Mapping(source = "workTime", target = "workTime", qualifiedByName = "jsonToWorkTimeDto")
    @Mapping(target = "applications", expression = "java(0)") // Service sẽ tính toán
    JobDashboardDto toDashboardDto(JobEntity entity);

    List<JobDetailDto> toDetailDtoList(List<JobEntity> entities);
    List<JobDashboardDto> toDashboardDtoList(List<JobEntity> entities);

    // Helper: Mapping EmployerEntity -> CompanyInJobDto (lồng nhau)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "socialMedias", target = "socialMedias", qualifiedByName = "jsonToSocialMediaList")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localDateTimeToInstant")
    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "localDateTimeToInstant")
    CompanyInJobDto employerToCompanyInJobDto(EmployerEntity employer);

    // --- DTO to Entity ---

    @Mapping(target = "skills", source = "skills", qualifiedByName = "objectToJsonString")
    @Mapping(target = "responsibilities", source = "responsibilities", qualifiedByName = "objectToJsonString")
    @Mapping(target = "skillAndExperiences", source = "skillAndExperience", qualifiedByName = "objectToJsonString")
    @Mapping(target = "jobType", source = "jobType", qualifiedByName = "objectToJsonString")
    @Mapping(target = "salary", source = "salary", qualifiedByName = "objectToJsonString")
    @Mapping(target = "workTime", source = "workTime", qualifiedByName = "objectToJsonString")
    JobEntity toEntity(CreateJobDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employer", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "skills", source = "skills", qualifiedByName = "objectToJsonString")
    @Mapping(target = "responsibilities", source = "responsibilities", qualifiedByName = "objectToJsonString")
    @Mapping(target = "skillAndExperiences", source = "skillAndExperience", qualifiedByName = "objectToJsonString")
    @Mapping(target = "jobType", source = "jobType", qualifiedByName = "objectToJsonString")
    @Mapping(target = "salary", source = "salary", qualifiedByName = "objectToJsonString")
    @Mapping(target = "workTime", source = "workTime", qualifiedByName = "objectToJsonString")
    void updateEntityFromDto(UpdateJobDto dto, @MappingTarget JobEntity entity);

    // --- Helper Methods (Qualified By Name) ---

    @Named("longToString")
    default String longToString(Long id) {
        return id != null ? String.valueOf(id) : null;
    }

    @Named("localDateTimeToDateString")
    default String localDateTimeToDateString(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DATE_FORMATTER);
    }

    @Named("localDateToDateString")
    default String localDateToDateString(LocalDate date) {
        if (date == null) return null;
        return date.format(DATE_FORMATTER);
    }

    @Named("localDateTimeToInstant")
    default Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}