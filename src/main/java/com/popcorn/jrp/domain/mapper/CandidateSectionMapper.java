package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.CandidateSectionEntity;
import com.popcorn.jrp.domain.request.candidate.CreateCandidateAboutDto;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateAboutDto;
import com.popcorn.jrp.domain.response.candidate.CandidateAboutDto;
import com.popcorn.jrp.domain.response.candidate.CandidateSectionBlockDto;
import org.mapstruct.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Mapper để chuyển đổi giữa CandidateSectionEntity và các DTO liên quan.
 */
@Mapper(componentModel = "spring")
public interface CandidateSectionMapper {

    // Định dạng formatter cho DTO
    // (Lưu ý: Bạn có thể thay đổi Locale nếu cần)
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("MMM dd, yyyy", Locale.ENGLISH);

    // =================================================================
    // === DTO -> Entity (Dùng cho Create)
    // =================================================================

    @Mapping(source = "category", target = "industry")
    @Mapping(target = "startTime", source = "startTime", qualifiedByName = "instantToLocalDate")
    @Mapping(target = "endTime", source = "endTime", qualifiedByName = "instantToLocalDate")
    @Mapping(target = "candidate", ignore = true) // Service sẽ thiết lập quan hệ này
    @Mapping(target = "id", ignore = true)        // Sẽ được tạo bởi DB
    @Mapping(target = "createdAt", ignore = true) // Sẽ được xử lý bởi @PrePersist
    @Mapping(target = "updatedAt", ignore = true) // Sẽ được xử lý bởi @PreUpdate
    CandidateSectionEntity toEntity(CreateCandidateAboutDto dto);

    // =================================================================
    // === Entity -> DTO (Dùng cho Response Create/Update)
    // =================================================================

    @Mapping(source = "industry", target = "category")
    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "startTime", target = "startTime", qualifiedByName = "localDateToInstant")
    @Mapping(source = "endTime", target = "endTime", qualifiedByName = "localDateToInstant")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localDateTimeToInstant")
    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "localDateTimeToInstant")
    CandidateAboutDto toCandidateAboutDto(CandidateSectionEntity entity);

    // =================================================================
    // === Entity -> DTO (Dùng cho Response GET List)
    // =================================================================

    @Mapping(target = "meta", expression = "java(entity.getTitle().substring(0,1))")
    @Mapping(target = "time", source = "entity", qualifiedByName = "formatTimeRange")
    CandidateSectionBlockDto toCandidateSectionBlockDto(CandidateSectionEntity entity);

    // MapStruct sẽ tự động sử dụng toCandidateSectionBlockDto cho list này
    List<CandidateSectionBlockDto> toCandidateSectionBlockDtoList(List<CandidateSectionEntity> entities);

    // =================================================================
    // === DTO -> Entity (Dùng cho Update/PATCH)
    // =================================================================

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "category", target = "industry")
    @Mapping(target = "startTime", source = "startTime", qualifiedByName = "instantToLocalDate")
    @Mapping(target = "endTime", source = "endTime", qualifiedByName = "instantToLocalDate")
    @Mapping(target = "candidate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true) // Sẽ được xử lý bởi @PreUpdate
    void updateEntityFromDto(UpdateCandidateAboutDto dto, @MappingTarget CandidateSectionEntity entity);

    // =================================================================
    // === Helper Methods (Qualified By Name)
    // =================================================================

    @Named("instantToLocalDate")
    default LocalDate instantToLocalDate(Instant instant) {
        if (instant == null) return null;
        // Chuyển Instant (UTC) về múi giờ hệ thống để lấy ngày
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Named("localDateToInstant")
    default Instant localDateToInstant(LocalDate localDate) {
        if (localDate == null) return null;
        // Chuyển ngày (không có múi giờ) thành Instant (UTC) tại thời điểm 00:00
        // theo múi giờ hệ thống
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    @Named("localDateTimeToInstant")
    default Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        // Chuyển LDT (theo múi giờ hệ thống) sang Instant (UTC)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    @Named("formatTimeRange")
    default String formatTimeRange(CandidateSectionEntity entity) {
        LocalDate startTime = entity.getStartTime();
        LocalDate endTime = entity.getEndTime();

        if (startTime == null) {
            return "N/A";
        }

        String formattedStart = startTime.format(DATE_FORMATTER);

        if (endTime == null) {
            return formattedStart + " - Present";
        }

        String formattedEnd = endTime.format(DATE_FORMATTER);
        return formattedStart + " - " + formattedEnd;
    }
}
