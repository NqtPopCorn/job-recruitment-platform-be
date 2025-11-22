package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.dto.aicore.AiCvParsingResponse;
import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.repository.CandidateRepository;
import com.popcorn.jrp.security.JwtUtil;
import com.popcorn.jrp.service.impl.AiCandidateServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai-cv")
@RequiredArgsConstructor
public class AiCvController {

    private final AiCandidateServiceImpl aiCandidateService;
    private final JwtUtil jwtUtil;
    private final CandidateRepository candidateRepository;

    // Sửa URL: Bỏ {candidateId} vì sẽ lấy từ token
    @PostMapping("/improve")
    public ResponseEntity<?> improveCv(Authentication authentication) {
        try {
            // 1. Lấy Token từ Header (Dạng: "Bearer eyJhbGci...")
            String userId = authentication.getName();

            // 2. Tìm Candidate ID từ User ID
            // Logic: Một User đăng nhập sẽ sở hữu một Candidate Profile
            CandidateEntity candidate = candidateRepository.findByUserId(Long.valueOf(userId))
                    .orElseThrow(() -> new RuntimeException("Candidate profile not found for this user"));

            // 3. Gọi Service với Candidate ID vừa tìm được
            // (Service giữ nguyên không cần sửa gì)
            AiCvParsingResponse improvedJson = aiCandidateService.improveAndReturnCv(candidate.getId());

            return ResponseEntity.ok(improvedJson);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid User ID in token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}