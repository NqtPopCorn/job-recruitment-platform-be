package com.popcorn.jrp.domain.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.jrp.domain.response.candidate.CompanyInfo;
import com.popcorn.jrp.domain.response.candidate.JobAppliedRecentlyResponse;
import com.popcorn.jrp.domain.response.candidate.SalaryRange;
import com.popcorn.jrp.domain.response.employer.CandidateInfo;
import com.popcorn.jrp.domain.response.employer.JobInfo;
import com.popcorn.jrp.domain.response.employer.RecentApplicantResponse;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.popcorn.jrp.domain.entity.ApplicationEntity;
import com.popcorn.jrp.domain.response.application.ApplicantResponseDto;
import com.popcorn.jrp.domain.response.application.ApplicationPageResponseDto;
import com.popcorn.jrp.domain.response.application.ApplicationResponseDto;
import com.popcorn.jrp.helper.JsonMapperHelper;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false), uses = { JsonMapperHelper.class,
        JobMapper.class, CandidateMapper.class })
public interface ApplicationMapper {

    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "job.id", target = "jobId")
    ApplicationResponseDto toApplicationResponseDto(ApplicationEntity entity);

    ApplicationPageResponseDto toApplicationPageResponseDto(ApplicationEntity entity);

    ApplicantResponseDto toApplicantResponseDto(ApplicationEntity entity);

    @Mapping(target = "applicationId", source = "id")
    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "jobTitle", source = "job.title")
    @Mapping(target = "company", source = "job.employer", qualifiedByName = "toCompanyInfo")
    @Mapping(target = "location", source = "job", qualifiedByName = "buildLocation")
    @Mapping(target = "salary", source = "job", qualifiedByName = "toSalaryRange")
    @Mapping(target = "jobTypes", source = "job.jobTypes")
    @Mapping(target = "appliedAt", source = "appliedAt")
    @Mapping(target = "appliedTimeAgo", source = "appliedAt", qualifiedByName = "calculateTimeAgo")
    @Mapping(target = "applicationStatus", source = "status")
    @Mapping(target = "isSaved", constant = "false") // Cần implement logic check bookmark
    JobAppliedRecentlyResponse toJobAppliedRecentlyResponse(ApplicationEntity entity);

    @Mapping(target = "applicationId", source = "id")
    @Mapping(target = "candidateId", source = "candidate.id")
    @Mapping(target = "candidateName", source = "candidate.name")
    @Mapping(target = "candidateAvatar", source = "candidate.avatar")
    @Mapping(target = "designation", source = "candidate.designation")
    @Mapping(target = "candidateInfo", source = "candidate", qualifiedByName = "toCandidateInfo")
    @Mapping(target = "jobInfo", source = "job", qualifiedByName = "toJobInfo")
    @Mapping(target = "appliedAt", source = "appliedAt")
    @Mapping(target = "appliedTimeAgo", source = "appliedAt", qualifiedByName = "calculateTimeAgo")
    @Mapping(target = "applicationStatus", source = "status")
    @Mapping(target = "coverLetter", source = "coverLetter")
    @Mapping(target = "resumeFilename", source = "filename")
    RecentApplicantResponse toRecentApplicantResponse(ApplicationEntity entity);


    @Named("toCompanyInfo")
    default CompanyInfo toCompanyInfo(com.popcorn.jrp.domain.entity.EmployerEntity employer) {
        if (employer == null) return null;
        return CompanyInfo.builder()
                .employerId(employer.getId())
                .name(employer.getName())
                .logo(employer.getLogo())
                .build();
    }

    @Named("toCandidateInfo")
    default CandidateInfo toCandidateInfo(com.popcorn.jrp.domain.entity.CandidateEntity candidate) {
        if (candidate == null) return null;

        // Parse skills from JSON
        List<String> skills = null;
        if (candidate.getSkills() != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                skills = mapper.readValue(
                        candidate.getSkills().toString(),
                        new TypeReference<List<String>>() {}
                );
            } catch (Exception e) {
                skills = List.of();
            }
        }

        String location = null;
        if (candidate.getCity() != null && candidate.getCountry() != null) {
            location = candidate.getCity() + ", " + candidate.getCountry();
        }

        return CandidateInfo.builder()
                .email(candidate.getEmail())
                .phone(candidate.getPhone())
                .location(location)
                .experience(candidate.getExperience())
                .skills(skills)
                .educationLevel(candidate.getEducationLevel())
                .build();
    }

    @Named("toJobInfo")
    default JobInfo toJobInfo(com.popcorn.jrp.domain.entity.JobEntity job) {
        if (job == null) return null;

        String location = null;
        if (job.getCity() != null && job.getCountry() != null) {
            location = job.getCity() + ", " + job.getCountry();
        }

        return JobInfo.builder()
                .jobId(job.getId())
                .jobTitle(job.getTitle())
                .location(location)
                .build();
    }

    @Named("buildLocation")
    default String buildLocation(com.popcorn.jrp.domain.entity.JobEntity job) {
        if (job == null) return null;
        return job.getCity() + ", " + job.getCountry();
    }

    @Named("toSalaryRange")
    default SalaryRange toSalaryRange(com.popcorn.jrp.domain.entity.JobEntity job) {
        if (job == null) return null;

        BigDecimal min = job.getMinSalary();
        BigDecimal max = job.getMaxSalary();
        String currency = job.getCurrency();

        String displayText = formatSalaryDisplay(min, max, currency);

        return SalaryRange.builder()
                .min(min)
                .max(max)
                .currency(currency)
                .displayText(displayText)
                .build();
    }

    default String formatSalaryDisplay(BigDecimal min, BigDecimal max, String currency) {
        if (min == null && max == null) return "Negotiable";

        String symbol = "$"; // Default USD
        if ("VND".equals(currency)) symbol = "₫";
        else if ("EUR".equals(currency)) symbol = "€";
        else if ("GBP".equals(currency)) symbol = "£";

        String minStr = min != null ? formatNumber(min) : "";
        String maxStr = max != null ? formatNumber(max) : "";

        if (min != null && max != null) {
            return symbol + minStr + " - " + symbol + maxStr;
        } else if (min != null) {
            return "From " + symbol + minStr;
        } else {
            return "Up to " + symbol + maxStr;
        }
    }

    default String formatNumber(BigDecimal number) {
        if (number == null) return "";

        double value = number.doubleValue();
        if (value >= 1000000) {
            return String.format("%.1fM", value / 1000000);
        } else if (value >= 1000) {
            return String.format("%.0fk", value / 1000);
        }
        return String.format("%.0f", value);
    }

    @Named("calculateTimeAgo")
    default String calculateTimeAgo(LocalDateTime appliedAt) {
        if (appliedAt == null) return "";

        Duration duration = Duration.between(appliedAt, LocalDateTime.now());

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;

        if (years > 0) return years + " year" + (years > 1 ? "s" : "") + " ago";
        if (months > 0) return months + " month" + (months > 1 ? "s" : "") + " ago";
        if (weeks > 0) return weeks + " week" + (weeks > 1 ? "s" : "") + " ago";
        if (days > 0) return days + " day" + (days > 1 ? "s" : "") + " ago";
        if (hours > 0) return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        if (minutes > 0) return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        return "Just now";
    }
}
