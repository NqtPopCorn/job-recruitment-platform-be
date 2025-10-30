package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.response.candidate.ResumeResponseDto;
import com.popcorn.jrp.domain.response.upload.UploadDataResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CandidateUploadService {
    String getAvatarUrl(Long candidateId);
    List<UploadDataResponse> getAllImageUrl(Long candidateId);

    String uploadAvatar(Long candidateId, MultipartFile file);
    UploadDataResponse uploadImage(Long candidateId, MultipartFile file);
    ResumeResponseDto uploadResume(Long candidateId, MultipartFile file, Boolean status);

    void deleteAvatar(Long candidateId);
    void deleteImage(Long imageId);
    void deleteResume(Long resumeId);
}
