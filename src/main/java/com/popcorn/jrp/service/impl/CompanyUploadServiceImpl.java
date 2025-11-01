package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.entity.EmployerImageEntity;
import com.popcorn.jrp.domain.response.upload.UploadDataResponse;
import com.popcorn.jrp.exception.NotFoundException; // Dùng exception nhất quán
import com.popcorn.jrp.repository.EmployerImageRepository;
import com.popcorn.jrp.repository.EmployerRepository;
import com.popcorn.jrp.service.CompanyUploadService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // Dùng StringUtils
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyUploadServiceImpl implements CompanyUploadService {

    private final EmployerRepository employerRepository;
    private final EmployerImageRepository employerImageRepository;

    private final Path logoRootLocation;
    private final Path imageRootLocation;

    private static final String PUBLIC_URL_PREFIX = "images/companies";

    @Autowired
    public CompanyUploadServiceImpl(EmployerRepository employerRepository,
                                    EmployerImageRepository employerImageRepository,
                                    @Value("${upload.path.company-images}") String companyPath) {
        this.employerRepository = employerRepository;
        this.employerImageRepository = employerImageRepository;

        // Giả sử logo và image chung 1 thư mục, giống như Candidate
        this.logoRootLocation = Paths.get(companyPath);
        this.imageRootLocation = Paths.get(companyPath);
    }

    // 2. Dùng @PostConstruct để khởi tạo thư mục
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(logoRootLocation);
            Files.createDirectories(imageRootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage locations", e);
        }
    }

    // --- CÁC HÀM GET URL (Triển khai Interface mới) ---

    @Override
    public String generateFileUrl(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        // Ví dụ: /images/companies/abc-123.jpg
        return "/" + PUBLIC_URL_PREFIX + "/" + fileName;
    }

    @Override
    @Transactional(readOnly = true)
    public String getLogoUrl(Long companyId) {
        EmployerEntity employer = getEmployerById(companyId);
        return generateFileUrl(employer.getLogo());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UploadDataResponse> getAllUploadedImage(Long companyId) {
        EmployerEntity employer = getEmployerById(companyId);

        return employer.getImages().stream()
                .map(image -> UploadDataResponse.builder()
                        .id(image.getId())
                        .url(generateFileUrl(image.getFilename()))
                        .build())
                .collect(Collectors.toList());
    }

    // --- CÁC HÀM UPLOAD (Triển khai Interface mới) ---

    @Override
    @Transactional
    public String uploadLogo(Long companyId, MultipartFile file) {
        EmployerEntity employer = getEmployerById(companyId);

        // Xóa logo cũ (giống logic deleteAvatar)
        if (employer.getLogo() != null && !employer.getLogo().isEmpty()) {
            deleteFile(employer.getLogo(), logoRootLocation);
        }

        // Lưu file mới
        String newFilename = storeFile(file, logoRootLocation);

        // Cập nhật entity
        employer.setLogo(newFilename);
        employerRepository.save(employer);

        return generateFileUrl(newFilename);
    }

    @Override
    @Transactional
    public UploadDataResponse uploadImage(Long companyId, MultipartFile file) {
        EmployerEntity employer = getEmployerById(companyId);
        String filename = storeFile(file, imageRootLocation);

        EmployerImageEntity imageEntity = new EmployerImageEntity();
        imageEntity.setEmployer(employer);
        imageEntity.setFilename(filename);
        imageEntity = employerImageRepository.save(imageEntity);

        return UploadDataResponse.builder()
                .id(imageEntity.getId())
                .url(generateFileUrl(filename))
                .build();
    }

    // --- CÁC HÀM DELETE (Triển khai Interface mới) ---

    @Override
    @Transactional
    public void deleteLogo(Long companyId) {
        EmployerEntity employer = getEmployerById(companyId);
        String filename = employer.getLogo();

        if (filename != null && !filename.isEmpty()) {
            deleteFile(filename, logoRootLocation);
            employer.setLogo(null);
            employerRepository.save(employer);
        }
    }

    @Override
    @Transactional
    public void deleteImageById(Long imageId) {
        EmployerImageEntity imageEntity = getEmployerImageById(imageId);
        deleteFile(imageEntity.getFilename(), imageRootLocation);
        employerImageRepository.delete(imageEntity);
    }

    @Override
    public void deleteImageByFileName(String fileName) {
        EmployerImageEntity imageEntity = employerImageRepository.findByFilename(fileName)
                .orElseThrow(() -> new RuntimeException("Image with name " + fileName));
        deleteFile(imageEntity.getFilename(), imageRootLocation);
        employerImageRepository.delete(imageEntity);
    }

    // --- CÁC HÀM HELPER PRIVATE (Tái sử dụng từ CandidateUploadServiceImpl) ---

    /**
     * Tìm Employer bằng ID, ném NotFoundException nếu không thấy.
     */
    private EmployerEntity getEmployerById(Long employerId) {
        return employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer with id " + employerId));
    }

    /**
     * Tìm EmployerImage bằng ID, ném NotFoundException nếu không thấy.
     */
    private EmployerImageEntity getEmployerImageById(Long imageId) {
        return employerImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("EmployerImage with id " + imageId));
    }

    /**
     * Lưu file vào một thư mục (location) với tên file duy nhất (UUID).
     */
    private String storeFile(MultipartFile file, Path location) {
        if (file == null || file.isEmpty()) {
            // Có thể ném một exception cụ thể hơn, ví dụ BadRequestException
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
            // Log lỗi này thay vì chỉ in ra
            System.err.println("Failed to delete file: " + filename + " from " + location);
        }
    }


}