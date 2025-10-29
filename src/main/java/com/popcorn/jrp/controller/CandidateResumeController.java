package com.popcorn.jrp.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller upload, provide candidate resume
 */
@RestController
@RequestMapping("/api/v1/resume")
public class CandidateResumeController {
    ///api/v1/resume/candidate/:id

    @GetMapping("/candidate/:candidateId")
    public ResponseEntity findAllCandidateResume(@PathVariable("candidateId") Long candidateId) {
        return null;
    }

    @PostMapping(value = "/candidate/:candidateId", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadCandidateResume(@PathVariable("candidateId") Long candidateId,
                                                @RequestParam(name = "file") MultipartFile file) {
        return null;
    }

    @GetMapping("/:resumeId")
    public ResponseEntity findResume(@PathVariable("resumeId") Long resumeId) {
        return null;
    }
}
