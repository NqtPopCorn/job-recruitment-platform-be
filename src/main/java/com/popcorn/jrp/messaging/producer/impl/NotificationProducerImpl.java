package com.popcorn.jrp.messaging.producer.impl;

import java.util.Optional;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.jrp.domain.dto.notification.ApplicationStatusDto;
import com.popcorn.jrp.domain.dto.notification.JobNotificationDto;
import com.popcorn.jrp.domain.entity.ApplicationEntity;
import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.domain.enums.NotificationType;
import com.popcorn.jrp.messaging.producer.NotificationProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducerImpl implements NotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private static final String JOB_CREATED_TOPIC = "job-created";

    private static final String APPLICATION_STATUS_TOPIC = "job-status";

    // Gửi message tao notification job created toi kafka
    @Override
    public void sendJobCreatedCreatedNotification(JobEntity entity) {
        try {
            String message = "["
                    + (entity.getEmployer() != null ? entity.getEmployer().getName() : "Unknown")
                    + "] has posted a new job: "
                    + (entity.getTitle() != null ? entity.getTitle() : "No title");

            JobNotificationDto dto = JobNotificationDto.builder()
                    .jobId(entity.getId())
                    .messsage(message)
                    .type(NotificationType.CREATED)
                    .build();

            String msg = objectMapper.writeValueAsString(dto);
            log.info("Sending job created notification message to Kafka: {}", msg);
            kafkaTemplate.send(JOB_CREATED_TOPIC, msg);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize JobForEmailDto", e);
        } catch (Exception e) {
            log.error("Failed to send message to Kafka", e);
        }
    }

    // Gửi message tao notification application status toi kafka
    @Override
    public void sendApplicationStatusNotification(ApplicationEntity entity) {
        try {
            String employerName = Optional.ofNullable(entity.getJob())
                    .map(JobEntity::getEmployer)
                    .map(EmployerEntity::getName)
                    .orElse("Unknown");

            String jobTitle = Optional.ofNullable(entity.getJob())
                    .map(JobEntity::getTitle)
                    .orElse("Unknown Job");

            String base = String.format("[%s] - Job: %s - ", employerName, jobTitle);

            String statusMessage;

            switch (entity.getStatus()) {
                case PENDING:
                    statusMessage = "Your application has been submitted successfully.";
                    break;

                case REVIEWED:
                    statusMessage = "Your application is currently under review.";
                    break;

                case ACCEPTED:
                    statusMessage = "Congratulations! Your application has been accepted.";
                    break;

                case REJECTED:
                    statusMessage = "We appreciate your interest, but your application has been rejected.";
                    break;

                default:
                    statusMessage = "Your application status has been updated.";
            }

            String message = base + statusMessage;

            ApplicationStatusDto dto = ApplicationStatusDto.builder()
                    .userId(entity.getCandidate().getUser().getId())
                    .jobId(entity.getJob().getId())
                    .message(message)
                    .type(NotificationType.APPLICATION_STATUS)
                    .build();

            String msg = objectMapper.writeValueAsString(dto);
            log.info("Sending application status notification message to Kafka: {}", msg);
            kafkaTemplate.send(APPLICATION_STATUS_TOPIC, msg);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize JobForEmailDto", e);
        } catch (Exception e) {
            log.error("Failed to send message to Kafka", e);
        }
    }

    @Override
    public void sendApplicantAppliesNotification(ApplicationEntity entity) {
        try {
            String jobTitle = Optional.ofNullable(entity.getJob())
                    .map(JobEntity::getTitle)
                    .orElse("Unknown Job");

            String candidateName = Optional.ofNullable(entity.getCandidate())
                    .map(CandidateEntity::getName)
                    .orElse("Unknown Candidate");

            String message = String.format("[%s] - Candidate: %s - has applied for this position",
                    jobTitle, candidateName);

            ApplicationStatusDto dto = ApplicationStatusDto.builder()
                    .userId(entity.getCandidate().getUser().getId())
                    .jobId(entity.getJob().getId())
                    .message(message)
                    .type(NotificationType.APPLICATION_STATUS)
                    .build();

            String msg = objectMapper.writeValueAsString(dto);
            log.info("Sending application status notification message to Kafka: {}", msg);
            kafkaTemplate.send(APPLICATION_STATUS_TOPIC, msg);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize JobForEmailDto", e);
        } catch (Exception e) {
            log.error("Failed to send message to Kafka", e);
        }
    }
}
