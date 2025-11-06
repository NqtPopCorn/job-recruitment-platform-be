package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.upload.UploadDataResponse;
import com.popcorn.jrp.service.CompanyUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/company-upload")
@RequiredArgsConstructor
public class CompanyUploadController {

        // private final CompanyUploadService companyUploadService;

        // @GetMapping("/{companyId}/logo")
        // public ResponseEntity<ApiDataResponse<Map<String, String>>>
        // getLogoOfCompanyById(@PathVariable("companyId") Long id) {
        // String logo = companyUploadService.getLogoUrl(id);

        // ApiDataResponse<Map<String, String>> response = ApiDataResponse.<Map<String,
        // String>>builder()
        // .statusCode(HttpStatus.OK.value())
        // .message("Lấy logo công ty thành công!")
        // .data(Map.of("url", logo))
        // .build();

        // return ResponseEntity.ok(response);
        // }

        // @GetMapping("/{companyId}/gallery")
        // public ResponseEntity<ApiDataResponse<List<UploadDataResponse>>>
        // getImagesOfCompanyById(@PathVariable("companyId") Long id) {
        // List<UploadDataResponse> images =
        // companyUploadService.getAllUploadedImage(id);

        // ApiDataResponse<List<UploadDataResponse>> response =
        // ApiDataResponse.<List<UploadDataResponse>>builder()
        // .statusCode(HttpStatus.OK.value())
        // .message("Lấy ảnh công ty thành công!")
        // .data(images != null ? images : List.of())
        // .build();

        // return ResponseEntity.ok(response);
        // }

        // @PostMapping(value = "/{companyId}/gallery", consumes =
        // MediaType.MULTIPART_FORM_DATA_VALUE)
        // public ResponseEntity<ApiDataResponse<UploadDataResponse>>
        // uploadCompanyImageFile(
        // @PathVariable("companyId") Long companyId,
        // @RequestParam("file") MultipartFile file
        // ) {
        // var res = companyUploadService.uploadImage(companyId, file);

        // ApiDataResponse<UploadDataResponse> response =
        // ApiDataResponse.<UploadDataResponse>builder()
        // .statusCode(HttpStatus.OK.value())
        // .message("Upload ảnh công ty thành công!")
        // .data(res)
        // .build();

        // return ResponseEntity.ok(response);
        // }

        // @PostMapping(value = "/{companyId}/logo", consumes =
        // MediaType.MULTIPART_FORM_DATA_VALUE)
        // public ResponseEntity<ApiDataResponse<Map<String, String>>>
        // uploadCompanyLogoFile(
        // @PathVariable("companyId") Long companyId,
        // @RequestParam("file") MultipartFile file
        // ) {
        // String result = companyUploadService.uploadLogo(companyId, file);

        // ApiDataResponse<Map<String, String>> response = ApiDataResponse.<Map<String,
        // String>>builder()
        // .statusCode(HttpStatus.OK.value())
        // .message("Upload logo công ty thành công!")
        // .data(Map.of("url", result))
        // .build();

        // return ResponseEntity.ok(response);
        // }

        // @DeleteMapping("/gallery/{imageId}")
        // public ResponseEntity<ApiDataResponse<Boolean>> deleteImageCompany(
        // @PathVariable("imageId") Long imageId
        // ) {
        // companyUploadService.deleteImageById(imageId);

        // ApiDataResponse<Boolean> response = ApiDataResponse.<Boolean>builder()
        // .statusCode(HttpStatus.OK.value())
        // .message("Xóa ảnh công ty thành công!")
        // .build();

        // return ResponseEntity.ok(response);
        // }

        // @DeleteMapping("/{companyId}/logo")
        // public ResponseEntity<ApiDataResponse> deleteLogoCompany(
        // @PathVariable("companyId") Long id
        // ) {
        // companyUploadService.deleteLogo(id);

        // ApiDataResponse<Boolean> response = ApiDataResponse.<Boolean>builder()
        // .statusCode(HttpStatus.OK.value())
        // .message("Xóa logo công ty thành công!")
        // .build();

        // return ResponseEntity.ok(response);
        // }

}
