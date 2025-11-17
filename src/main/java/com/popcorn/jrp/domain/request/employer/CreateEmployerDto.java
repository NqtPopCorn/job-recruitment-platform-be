package com.popcorn.jrp.domain.request.employer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateEmployerDto {

    @NotNull
    private Long userId;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Tên nhà tuyển dụng không được để trống")
    private String name;

    @NotBlank(message = "Ngành nghề chính không được để trống")
    private String primaryIndustry;

    @NotBlank(message = "Quy mô nhà tuyển dụng không được để trống")
    private String size;

    @NotNull(message = "Năm thành lập không được để trống")
    private Integer foundedIn;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Quốc gia không được để trống")
    private String country;

    private String city; // Tùy chọn

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    private String logo; // Tùy chọn

    private String website; // Tùy chọn
}
