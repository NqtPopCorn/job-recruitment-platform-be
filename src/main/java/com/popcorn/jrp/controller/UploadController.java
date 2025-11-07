package com.popcorn.jrp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiResultsResponse;
import com.popcorn.jrp.domain.response.candidate.ResumeResponseDto;
import com.popcorn.jrp.domain.response.upload.UploadDataResponse;
import com.popcorn.jrp.service.CandidateUploadService;
import com.popcorn.jrp.service.CompanyUploadService;
import com.popcorn.jrp.service.ResumeService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadController {

        private final CompanyUploadService companyUploadService;

        private final CandidateUploadService candidateUploadService;

        private final ResumeService resumeService;

        // <!-- Company Logo And Images -->
        @GetMapping("/logo/company/{companyId}")
        public ResponseEntity<ApiDataResponse<String>> getLogoOfCompanyById(
                        @PathVariable("companyId") Long id) {
                String logo = companyUploadService.getLogoUrl(id);

                ApiDataResponse<String> response = ApiDataResponse.<String>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy logo công ty thành công!")
                                .data(logo)
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/images/company/{companyId}")
        public ResponseEntity<ApiResultsResponse<UploadDataResponse>> getImagesOfCompanyById(
                        @PathVariable("companyId") Long id) {
                List<UploadDataResponse> images = companyUploadService.getAllUploadedImage(id);

                ApiResultsResponse<UploadDataResponse> response = ApiResultsResponse.<UploadDataResponse>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Lấy ảnh công ty thành công!")
                                .results(images != null ? images : List.of())
                                .build();

                return ResponseEntity.ok(response);
        }

        // UPLOAD IMAGE COMPANY
        @PostMapping(value = "/image/company/{companyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiDataResponse<UploadDataResponse>> uploadCompanyImageFile(
                        @PathVariable("companyId") Long companyId,
                        @RequestParam("file") MultipartFile file) {
                UploadDataResponse res = companyUploadService.uploadImage(companyId, file);

                ApiDataResponse<UploadDataResponse> response = ApiDataResponse.<UploadDataResponse>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Upload ảnh công ty thành công!")
                                .data(res)
                                .build();

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        // UPLOAD LOGO COMPANY
        @PostMapping(value = "/logo/company/{companyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiDataResponse<String>> uploadCompanyLogoFile(
                        @PathVariable("companyId") Long companyId,
                        @RequestParam("file") MultipartFile file) {
                String logoString = companyUploadService.uploadLogo(companyId, file);

                ApiDataResponse<String> response = ApiDataResponse.<String>builder()
                                .statusCode(HttpStatus.CREATED.value())
                                .message("Upload logo công ty thành công!")
                                .data(logoString)
                                .build();

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        // DELETE IMAGE COMPANY
        @DeleteMapping("/image/{imageId}/company")
        public ResponseEntity<ApiNoDataResponse> deleteImageCompany(
                        @PathVariable("imageId") Long imageId) {
                companyUploadService.deleteImageById(imageId);

                ApiNoDataResponse response = ApiNoDataResponse.builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Xóa ảnh công ty thành công!")
                                .build();

                return ResponseEntity.ok(response);
        }

        // DELETE LOGO COMPANY
        @DeleteMapping("/logo/company/{companyId}")
        public ResponseEntity<ApiNoDataResponse> deleteLogoCompany(
                        @PathVariable("companyId") Long id) {
                companyUploadService.deleteLogo(id);

                ApiNoDataResponse response = ApiNoDataResponse.builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Xóa logo công ty thành công!")
                                .build();

                return ResponseEntity.ok(response);
        }

        // <!-- Candidate Avatar And Images -->

        /**
         * Lấy URL của avatar
         */
        @GetMapping("/avatar/candidate/{candidateId}")
        public ResponseEntity<ApiDataResponse<String>> getAvatarUrl(
                        @PathVariable("candidateId") Long candidateId) {
                String url = candidateUploadService.getAvatarUrl(candidateId);
                return ResponseEntity.ok(ApiDataResponse.<String>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Get avatar of candidate successfully!")
                                .data(url)
                                .build());
        }

        /**
         * Upload avatar mới (hoặc thay thế avatar cũ)
         */
        @PostMapping(value = "/avatar/candidate/{candidateId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiDataResponse<String>> uploadAvatar(
                        @PathVariable("candidateId") Long candidateId,
                        @RequestParam("file") MultipartFile file) {
                String url = candidateUploadService.uploadAvatar(candidateId, file);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiDataResponse.<String>builder()
                                                .statusCode(HttpStatus.OK.value())
                                                .data(url)
                                                .build());
        }

        /**
         * Xóa avatar
         */
        @DeleteMapping("/avatar/candidate/{candidateId}")
        public ResponseEntity<ApiNoDataResponse> deleteAvatar(@PathVariable("candidateId") Long candidateId) {
                candidateUploadService.deleteAvatar(candidateId);
                return ResponseEntity.ok(ApiNoDataResponse.builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Delete avatar of candidate successfully!")
                                .build());
        }

        /**
         * Lấy danh sách URL của tất cả các ảnh trong gallery
         */
        @GetMapping("/images/candidate/{candidateId}")
        public ResponseEntity<ApiResultsResponse<UploadDataResponse>> getAllImageUrls(
                        @PathVariable("candidateId") Long candidateId) {
                List<UploadDataResponse> images = candidateUploadService.getAllImageUrl(candidateId);
                return ResponseEntity.ok(ApiResultsResponse.<UploadDataResponse>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Get images of candidate successfully!")
                                .results(images)
                                .build());
        }

        /**
         * Upload một ảnh mới vào gallery
         */
        @PostMapping(path = "/image/candidate/{candidateId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiDataResponse<UploadDataResponse>> uploadImage(
                        @PathVariable("candidateId") Long candidateId,
                        @RequestParam("file") MultipartFile file) {
                UploadDataResponse res = candidateUploadService.uploadImage(candidateId, file);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiDataResponse.<UploadDataResponse>builder()
                                                .statusCode(HttpStatus.CREATED.value())
                                                .message("Upload image of candidate successfully!")
                                                .data(res)
                                                .build());
        }

        /**
         * Xóa một ảnh khỏi gallery bằng filename của ảnh
         */
        @DeleteMapping("image/{imageId}/candidate")
        public ResponseEntity<ApiNoDataResponse> deleteImage(@PathVariable("imageId") Long imageId) {
                candidateUploadService.deleteImageById(imageId);
                return ResponseEntity.ok(ApiNoDataResponse.builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Delete image of candidate successfully!")
                                .build());
        }

        // <!-- Resumes -->
        /**
         * Lấy danh sách CV của ứng viên
         */
        @GetMapping("resume/candidate/{candidateId}")
        public ResponseEntity<ApiDataResponse<List<ResumeResponseDto>>> getResumesByCandidate(
                        @PathVariable("candidateId") Long candidateId) {
                List<ResumeResponseDto> results = resumeService.getResumesByCandidateId(candidateId);
                return ResponseEntity.ok(ApiDataResponse.<List<ResumeResponseDto>>builder()
                                .statusCode(200)
                                .data(results)
                                .message("Success")
                                .build());
        }

        /**
         * Tải lên CV mới
         */
        @PostMapping(value = "/resume/candidate/{candidateId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiDataResponse<ResumeResponseDto>> uploadResume(
                        @PathVariable("candidateId") Long candidateId,
                        @RequestParam("file") MultipartFile file,
                        @RequestParam(value = "status", defaultValue = "true") boolean status) {
                // Check limit
                resumeService.checkTheNumberOfResumesByCandidateId(candidateId);
                // Create new resume
                ResumeResponseDto result = resumeService.createResume(candidateId, file, status);
                return ResponseEntity.status(HttpStatus.CREATED).body(ApiDataResponse.<ResumeResponseDto>builder()
                                .statusCode(HttpStatus.CREATED.value())
                                .message("Upload resume of candidate successfully!")
                                .data(result)
                                .build());
        }

        /**
         * Xóa CV
         */
        @DeleteMapping("resume/{resumeId}")
        public ResponseEntity<ApiNoDataResponse> deleteResume(@PathVariable("resumeId") Long resumeId,
                        @RequestParam("filename") String filename) {
                resumeService.deleteResume(resumeId);
                return ResponseEntity.ok(ApiNoDataResponse.builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Delete successfully")
                                .build());
        }
}
