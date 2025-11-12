package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.helper.JsonMapperHelper;
import com.popcorn.jrp.domain.request.job.CreateJobDto;
import com.popcorn.jrp.domain.request.job.UpdateJobDto;
import com.popcorn.jrp.domain.response.job.CompanyInJobDto;
import com.popcorn.jrp.domain.response.job.JobDashboardDto;
import com.popcorn.jrp.domain.response.job.JobDetailDto;
import com.popcorn.jrp.domain.response.job.JobResponseDto;
import com.popcorn.jrp.domain.response.job.SalaryDto;
import com.popcorn.jrp.domain.response.job.WorkTimeDto;

import org.mapstruct.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", uses = { JsonMapperHelper.class, EmployerMapper.class })
public interface JobMapper extends PageMapper {

    // Định dạng ngày theo "d/M/yyyy" (ví dụ: 6/8/2025)
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");

    // --- Entity to DTO ---

    @Mapping(source = "employer.logo", target = "logo") // Lấy logo từ Employer
    @Mapping(source = "employer", target = "company") // Map lồng ghép
    @Mapping(source = "expirationDate", target = "expireDate")
    @Mapping(source = ".", target = "salary", qualifiedByName = "mapSalary")
    @Mapping(source = ".", target = "workTime", qualifiedByName = "mapWorkTimeDto")
    JobDetailDto toDetailDto(JobEntity entity);

    @Mapping(source = "employer", target = "company")
    @Mapping(source = ".", target = "applications", qualifiedByName = "mapApplicationsCount")
    @Mapping(source = "employer.logo", target = "logo")
    JobResponseDto toJobResponseDto(JobEntity entity);

    @InheritConfiguration(name = "toDetailDto")
    @Mapping(source = ".", target = "applications", qualifiedByName = "mapApplicationsCount")
    JobDashboardDto toDashboardDto(JobEntity entity);

    List<JobDashboardDto> toDashboardDtoList(List<JobEntity> entities);

    // Helper: Mapping EmployerEntity -> CompanyInJobDto (lồng nhau)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "socialMedias", target = "socialMedias", qualifiedByName = "jsonToSocialMediaList")
    CompanyInJobDto employerToCompanyInJobDto(EmployerEntity employer);

    // --- DTO to Entity ---
    /**
     * NEED CREATE !!!!!!!!!!!!
     */
    @Mapping(source = "salary.min", target = "minSalary")
    @Mapping(source = "salary.max", target = "maxSalary")
    @Mapping(source = "salary.currency", target = "currency")
    @Mapping(source = "salary.negotiable", target = "negotiable")
    @Mapping(source = "workTime.from", target = "workTimeFrom")
    @Mapping(source = "workTime.to", target = "workTimeTo")
    JobEntity toEntity(CreateJobDto dto);

    /**
     * NEED UPDATE !!!!!!!!!!!!
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employer", ignore = true)
    @Mapping(source = "salary.min", target = "minSalary")
    @Mapping(source = "salary.max", target = "maxSalary")
    @Mapping(source = "salary.currency", target = "currency")
    @Mapping(source = "salary.negotiable", target = "negotiable")
    @Mapping(source = "workTime.from", target = "workTimeFrom")
    @Mapping(source = "workTime.to", target = "workTimeTo")
    void updatePartialEntityFromDto(UpdateJobDto dto, @MappingTarget JobEntity entity);

    @Mapping(source = "salary.min", target = "minSalary")
    @Mapping(source = "salary.max", target = "maxSalary")
    @Mapping(source = "salary.currency", target = "currency")
    @Mapping(source = "salary.negotiable", target = "negotiable")
    @Mapping(source = "workTime.from", target = "workTimeFrom")
    @Mapping(source = "workTime.to", target = "workTimeTo")
    void updateEntityFromDto(UpdateJobDto dto, @MappingTarget JobEntity entity);

    // --- Helper Methods (Qualified By Name) ---
    @Named("localDateTimeToInstant")
    default Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return null;
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    @Named("mapApplicationsCount")
    default int mapApplicationsCount(JobEntity entity) {
        if (entity == null || entity.getApplications() == null)
            return 0;
        return entity.getApplications().size();
    }

    @Named("mapSalary")
    default SalaryDto mapSalary(JobEntity entity) {
        if (entity == null)
            return null;
        SalaryDto dto = SalaryDto.builder()
                .min(entity.getMinSalary())
                .max(entity.getMaxSalary())
                .currency(entity.getCurrency())
                .negotiable(entity.getNegotiable())
                .build();
        return dto;
    }

    @Named("mapWorkTimeDto")
    default WorkTimeDto mapWorkTimeDto(JobEntity entity) {
        if (entity == null)
            return null;
        WorkTimeDto dto = WorkTimeDto.builder()
                .from(entity.getWorkTimeFrom())
                .to(entity.getWorkTimeTo())
                .build();
        return dto;
    }
}
