package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.mapper.ResumeMapper;
import com.popcorn.jrp.domain.request.candidate.UpdateResumeDto;
import com.popcorn.jrp.domain.response.candidate.ResumeResponseDto;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.CandidateRepository;
import com.popcorn.jrp.repository.ResumeRepository;
import com.popcorn.jrp.service.CandidateUploadService;
import com.popcorn.jrp.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResumeResponseDto getResumeById(Long resumeId) {
        var resume = resumeRepository.findById(resumeId).orElseThrow(() -> new NotFoundException("Resume with id " + resumeId));
        return resumeMapper.toResponse(resume);
    }

    @Override
    @Transactional
    public ResumeResponseDto updateResume(Long resumeId, UpdateResumeDto updateDto) {
        var resume = resumeRepository.findById(resumeId).orElseThrow(() -> new NotFoundException("Resume with id " + resumeId));
        resumeMapper.updateEntity(resume, updateDto);

        return resumeMapper.toResponse(resumeRepository.save(resume));
    }

    @Override
    public void deleteResume(Long resumeId) {
        candidateUploadService.deleteResume(resumeId);
    }

    private CandidateEntity findCandidateById(Long candidateId) {
        return candidateRepository.findById(candidateId).orElseThrow(()-> new NotFoundException("Candidate with id " + candidateId));
    }
}
