package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.entity.CandidateImageEntity;
import com.popcorn.jrp.domain.entity.ResumeEntity;
import com.popcorn.jrp.domain.mapper.ResumeMapper;
import com.popcorn.jrp.domain.response.candidate.ResumeResponseDto;
import com.popcorn.jrp.domain.response.upload.UploadDataResponse;
import com.popcorn.jrp.exception.NotFoundException; // Import exception tùy chỉnh
import com.popcorn.jrp.repository.CandidateImageRepository;
import com.popcorn.jrp.repository.CandidateRepository;
import com.popcorn.jrp.repository.ResumeRepository;
import com.popcorn.jrp.service.CandidateUploadService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CandidateUploadServiceImpl implements CandidateUploadService {

    private final CandidateRepository candidateRepository;
    private final CandidateImageRepository candidateImageRepository;
    private final ResumeRepository resumeRepository;

    private final Path avatarRootLocation;
    private final Path imageRootLocation;
    private final Path resumeRootLocation;
    private final ResumeMapper resumeMapper;

    @Autowired
    public CandidateUploadServiceImpl(CandidateRepository candidateRepository,
                                      CandidateImageRepository candidateImageRepository,
                                      ResumeRepository resumeRepository,
                                      @Value("${upload.path.candidate-images}") String avatarPath,
                                      @Value("${upload.path.candidate-images}") String imagePath,
                                      @Value("${upload.path.resumes}") String resumePath,
                                      ResumeMapper resumeMapper) {
        this.candidateRepository = candidateRepository;
        this.candidateImageRepository = candidateImageRepository;
        this.resumeRepository = resumeRepository;
        this.avatarRootLocation = Paths.get(avatarPath);
        this.imageRootLocation = Paths.get(imagePath);
        this.resumeRootLocation = Paths.get(resumePath);
        this.resumeMapper = resumeMapper;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(avatarRootLocation);
            Files.createDirectories(imageRootLocation);
            Files.createDirectories(resumeRootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage locations", e);
        }
    }

    // --- CÁC HÀM GET URL ---

    @Override
    public String generateFileUrl(String fileName, String type) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        return switch (type) {
            case "image", "avatar" -> "/images/candidates/" + fileName;
            case "resume" -> "/resumes/" + fileName;
            default -> "";
        };
    }

    /**
     * Xây dựng URL để client có thể truy cập file.
     */
//    private String buildFileUrl(String pathPrefix, String filename) {
//        if (filename == null || filename.isEmpty()) {
//            return "";
//        }
//        // Ví dụ: /avatars/abc-123.jpg
//        return "/" + pathPrefix + "/" + filename;
//    }

    @Override
    @Transactional(readOnly = true)
    public String getAvatarUrl(Long candidateId) {
        CandidateEntity candidate = getCandidateById(candidateId);
        return generateFileUrl(candidate.getAvatar(), "avatar");
    }

    @Override
    @Transactional(readOnly = true)
    public List<UploadDataResponse> getAllImageUrl(Long candidateId) {
        CandidateEntity candidate = getCandidateById(candidateId);

        return candidate.getImages().stream()
                .map(image -> UploadDataResponse.builder()
                        .id(image.getId())
                        .url(generateFileUrl(image.getFilename(), "image"))
                        .build())
                .collect(Collectors.toList());
    }

//    @Override
//    @Transactional(readOnly = true)
//    public String getResumeUrl(Long resumeId) {
//        ResumeEntity resume = getResumeById(resumeId);
//        return buildFileUrl("resumes", resume.getFilename());
//    }

    // --- CÁC HÀM UPLOAD ---

    @Override
    @Transactional
    public String uploadAvatar(Long candidateId, MultipartFile file) {
        CandidateEntity candidate = getCandidateById(candidateId);

        // Xóa avatar cũ
        if (candidate.getAvatar() != null && !candidate.getAvatar().isEmpty()) {
            deleteFile(candidate.getAvatar(), avatarRootLocation);
        }

        // Lưu file mới
        String newFilename = storeFile(file, avatarRootLocation);

        // Cập nhật entity
        candidate.setAvatar(newFilename);
        candidateRepository.save(candidate);

        return generateFileUrl(candidate.getAvatar(), "avatar");
    }

    @Override
    @Transactional
    public UploadDataResponse uploadImage(Long candidateId, MultipartFile file) {
        CandidateEntity candidate = getCandidateById(candidateId);
        String filename = storeFile(file, imageRootLocation);

        CandidateImageEntity imageEntity = new CandidateImageEntity();
        imageEntity.setCandidate(candidate);
        imageEntity.setFilename(filename);
        imageEntity = candidateImageRepository.save(imageEntity);

        return UploadDataResponse.builder()
                .id(imageEntity.getId())
                .url(generateFileUrl(filename, "image"))
                .build();
    }

    @Override
    @Transactional
    public ResumeResponseDto uploadResume(Long candidateId, MultipartFile file, Boolean status) {
        CandidateEntity candidate = getCandidateById(candidateId);
        String filename = storeFile(file, resumeRootLocation);

        ResumeEntity resumeEntity = new ResumeEntity();
        resumeEntity.setCandidate(candidate);
        resumeEntity.setFileName(filename);
        resumeEntity.setStatus(status);
        resumeRepository.save(resumeEntity);

        var res = resumeMapper.toResponse(resumeEntity);
        res.setUrl(generateFileUrl(filename, "resume"));
        return res;
    }

    // --- CÁC HÀM DELETE ---

    @Override
    @Transactional
    public void deleteAvatar(Long candidateId) {
        CandidateEntity candidate = getCandidateById(candidateId);
        String filename = candidate.getAvatar();

        if (filename != null && !filename.isEmpty()) {
            deleteFile(filename, avatarRootLocation);
            candidate.setAvatar(null);
            candidateRepository.save(candidate);
        }
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        CandidateImageEntity imageEntity = getCandidateImageById(imageId);
        deleteFile(imageEntity.getFilename(), imageRootLocation);
        candidateImageRepository.delete(imageEntity);
    }

    @Override
    @Transactional
    public void deleteResume(Long resumeId) {
        ResumeEntity resumeEntity = getResumeById(resumeId);
        deleteFile(resumeEntity.getFileName(), resumeRootLocation);
        resumeRepository.delete(resumeEntity);
    }


    // --- CÁC HÀM HELPER PRIVATE ---

    /**
     * Tìm Candidate bằng ID, ném NotFoundException nếu không thấy.
     */
    private CandidateEntity getCandidateById(Long candidateId) {
        return candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate with id " + candidateId));
    }

    /**
     * Tìm Resume bằng ID, ném NotFoundException nếu không thấy.
     */
    private ResumeEntity getResumeById(Long resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new NotFoundException("Resume with id " + resumeId));
    }

    /**
     * Tìm CandidateImage bằng ID, ném NotFoundException nếu không thấy.
     */
    private CandidateImageEntity getCandidateImageById(Long imageId) {
        return candidateImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("CandidateImage with id " + imageId));
    }

    /**
     * Lưu file vào một thư mục (location) với tên file duy nhất (UUID).
     */
    private String storeFile(MultipartFile file, Path location) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID() + "." + extension;

        try (InputStream inputStream = file.getInputStream()) {
            Path targetLocation = location.resolve(uniqueFilename);
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + uniqueFilename, e);
        }
    }

    /**
     * Xóa file vật lý khỏi hệ thống.
     */
    private void deleteFile(String filename, Path location) {
        try {
            Path file = location.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + filename + " from " + location);
        }
    }


}