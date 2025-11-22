package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.dto.aicore.AiCvParsingResponse;

public interface AiCandidateService {
    void generateCvForCandidate(Long candidateId, String rawCvText);
    AiCvParsingResponse improveAndReturnCv(Long candidateId);
}
