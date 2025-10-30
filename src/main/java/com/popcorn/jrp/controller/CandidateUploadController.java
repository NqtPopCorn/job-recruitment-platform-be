package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.candidate.ResumeResponseDto;
import com.popcorn.jrp.domain.response.upload.UploadDataResponse;
import com.popcorn.jrp.service.CandidateUploadService;
import com.popcorn.jrp.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/candidate-upload")
@RequiredArgsConstructor
public class CandidateUploadController {

    private final CandidateUploadService candidateUploadService;
    private final ResumeService resumeService;

    /**
     * Lấy danh sách CV của ứng viên
     * GET /api/v1/candidate/:id/resume
     */
    @GetMapping("/{candidateId}/resume")
    public ResponseEntity<ApiDataResponse<List<ResumeResponseDto>>> getResumesByCandidate(
            @PathVariable("candidateId") Long candidateId
    ) {
        List<ResumeResponseDto> results = resumeService.getResumesByCandidateId(candidateId);
        return ResponseEntity.ok(ApiDataResponse.<List<ResumeResponseDto>>builder()
                .statusCode(200)
                .data(results)
                .message("Success")
                .build());
    }

    /**
     * Tải lên CV mới
     * POST /api/v1/candidate/:id/resume
     */
    @PostMapping(value = "/{candidateId}/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiDataResponse<ResumeResponseDto>> uploadResume(
            @PathVariable("candidateId") Long candidateId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "status", defaultValue = "true") boolean status
    ) {
        ResumeResponseDto result = resumeService.createResume(candidateId, file, status);
        return ResponseEntity.ok(ApiDataResponse.<ResumeResponseDto>builder()
                .statusCode(200)
                .data(result)
                .message("Success")
                .build());
    }

    /**
     * Lấy URL của avatar
     */
    @GetMapping("/{candidateId}/avatar")
    public ResponseEntity<ApiDataResponse<Map<String, String>>> getAvatarUrl(@PathVariable("candidateId") Long candidateId) {
        String url = candidateUploadService.getAvatarUrl(candidateId);
        return ResponseEntity.ok(ApiDataResponse.<Map<String, String>>builder()
                .statusCode(200)
                .data(Map.of("url", url))
                .build());
    }

    /**
     * Upload avatar mới (hoặc thay thế avatar cũ)
     */
    @PostMapping(value = "/{candidateId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiDataResponse<Map<String, String>>> uploadAvatar(@PathVariable("candidateId") Long candidateId,
                                                            @RequestParam("file") MultipartFile file) {
        // !! LƯU Ý BẢO MẬT:
        // Bạn nên thêm logic ở đây để kiểm tra xem người dùng đã được xác thực (authenticated)
        // có quyền sửa đổi candidateId này hay không.
        String url = candidateUploadService.uploadAvatar(candidateId, file);
        return ResponseEntity.ok(ApiDataResponse.<Map<String, String>>builder()
                .statusCode(200)
                .data(Map.of("url", url))
                .build());
    }

    /**
     * Xóa avatar
     */
    @DeleteMapping("/{candidateId}/avatar")
    public ResponseEntity<ApiDataResponse> deleteAvatar(@PathVariable("candidateId") Long candidateId) {
        // !! LƯU Ý BẢO MẬT: (Tương tự như trên)
        candidateUploadService.deleteAvatar(candidateId);
        return ResponseEntity.ok(ApiDataResponse.builder()
                .statusCode(200)
                .message("Delete successfully")
                .build());
    }

    // --- Candidate Images (Gallery) ---

    /**
     * Lấy danh sách URL của tất cả các ảnh trong gallery
     */
    @GetMapping("/{candidateId}/gallery")
    public ResponseEntity<ApiDataResponse<List<String>>> getAllImageUrls(@PathVariable("candidateId") Long candidateId) {
        List<String> urls = candidateUploadService.getAllImageUrl(candidateId);
        return ResponseEntity.ok(ApiDataResponse.<List<String>>builder()
                .statusCode(200)
                .data(urls)
                .build());
    }

    /**
     * Upload một ảnh mới vào gallery
     */
    @PostMapping(path = "/{candidateId}/gallery", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiDataResponse<UploadDataResponse>> uploadImage(
            @PathVariable("candidateId") Long candidateId,
            @RequestParam("file") MultipartFile file) {
        // !! LƯU Ý BẢO MẬT: (Tương tự như trên)
        var res = candidateUploadService.uploadImage(candidateId, file);
        return ResponseEntity.ok(ApiDataResponse.<UploadDataResponse>builder()
                .statusCode(200)
                .data(res)
                .build());
    }

    /**
     * Xóa một ảnh khỏi gallery bằng ID của ảnh
     */
    @DeleteMapping("/gallery/{imageId}")
    public ResponseEntity<ApiDataResponse> deleteImage(@PathVariable("imageId") Long imageId) {
        // !! LƯU Ý BẢO MẬT: (Tương tự như trên)
        candidateUploadService.deleteImage(imageId);
        return ResponseEntity.ok(ApiDataResponse.builder()
                .statusCode(200)
                .message("Delete successfully")
                .build());
    }

}