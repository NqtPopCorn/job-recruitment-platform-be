package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.mapper.ResumeMapper;
import com.popcorn.jrp.domain.response.candidate.ResumeResponseDto;
import com.popcorn.jrp.exception.BadRequestException;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.CandidateRepository;
import com.popcorn.jrp.repository.ResumeRepository;
import com.popcorn.jrp.service.CandidateUploadService;
import com.popcorn.jrp.service.ResumeService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final CandidateUploadService candidateUploadService;
    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;
    private final CandidateRepository candidateRepository;

    @Value("${limit.file.resume.upload:10}")
    private long limitFileResumeToUpload;

    @Override
    public List<ResumeResponseDto> getResumesByCandidateId(Long candidateId) {
        var found = findCandidateById(candidateId);
        return found.getResumes().stream().map(resumeMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResumeResponseDto createResume(Long candidateId, MultipartFile file, boolean status) {
        return candidateUploadService.uploadResume(candidateId, file, status);
    }

    @Override
    public void deleteResume(Long resumeId) {
        candidateUploadService.deleteResume(resumeId);
    }

    private CandidateEntity findCandidateById(Long candidateId) {
        return candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate with id " + candidateId));
    }

    @Override
    public void checkTheNumberOfResumesByCandidateId(Long candidateId) {
        long resumeOfCandidate = resumeRepository.countByCandidateId(candidateId);
        if (resumeOfCandidate > limitFileResumeToUpload) {
            throw new BadRequestException("Ứng viên đã đạt tối đa 10 CV, không thể upload thêm.");
        }
    }
}
