package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.response.upload.UploadDataResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * Service xử lý upload/download file cho Company
 * (Đã refactor để giống với CandidateUploadService)
 */
public interface CompanyUploadService {

    /**
     * Lấy URL logo của công ty.
     * Tương ứng với: getAvatarUrl(Long candidateId)
     *
     * @param companyId ID của công ty.
     * @return Đường dẫn (URL) của logo.
     */
    String getLogoUrl(Long companyId);

    /**
     * Lấy danh sách URL tất cả ảnh (gallery) của công ty.
     * Tương ứng với: getAllImageUrl(Long candidateId)
     *
     * @param companyId ID của công ty.
     * @return Danh sách đường dẫn (URL) ảnh.
     */
    List<UploadDataResponse> getAllUploadedImage(Long companyId);

    /**
     * Upload logo mới cho công ty (thường là ghi đè hoặc cập nhật).
     * Tương ứng với: uploadAvatar(Long candidateId, MultipartFile file)
     *
     * @param companyId ID của công ty.
     * @param file File logo tải lên.
     * @return Đường dẫn (URL) logo sau khi lưu.
     */
    String uploadLogo(Long companyId, MultipartFile file);

    /**
     * Upload một ảnh (gallery) mới cho công ty.
     * Tương ứng với: uploadImage(Long candidateId, MultipartFile file)
     *
     * @param companyId ID của công ty.
     * @param file File ảnh tải lên.
     * @return Đường dẫn (URL) ảnh sau khi lưu.
     */
    UploadDataResponse uploadImage(Long companyId, MultipartFile file);

    /**
     * Xóa logo của công ty.
     * Tương ứng với: deleteAvatar(Long candidateId)
     * (Vì logo là 1-1, chỉ cần companyId)
     *
     * @param companyId ID của công ty.
     */
    void deleteLogo(Long companyId);

    /**
     * Xóa một ảnh (gallery) của công ty bằng ID của ảnh.
     * Tương ứng với: deleteImage(Long imageId)
     *
     * @param imageId ID của ảnh cần xóa.
     */
    void deleteImageById(Long imageId);

    /**
     * Xóa một ảnh (gallery) của công ty bằng filename
     * Tương ứng với: deleteImage(Long imageId)
     *
     * @param fileName tên của ảnh cần xóa.
     */
    void deleteImageByFileName(String fileName);
}