package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.UserEntity;
import com.popcorn.jrp.domain.mapper.UserMapper;
import com.popcorn.jrp.domain.request.user.UserQueryAdmin;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.user.UserAdminResponse;
import com.popcorn.jrp.domain.response.user.UserStatusStatisticResponse;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.UserAdminRepository;
import com.popcorn.jrp.repository.spec.UserAdminSpecification;
import com.popcorn.jrp.service.UserAdminService;
import com.popcorn.jrp.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserAdminServiceV1 implements UserAdminService {
    UserAdminRepository userAdminRepository;
    UserAdminSpecification userAdminSpecification;
    UserMapper mapper;

    @Override
    public ApiPageResponse<UserAdminResponse> getUserListAdmin(UserQueryAdmin request, Pageable pageable) {
        try {
            Specification<UserEntity> spec = userAdminSpecification.getAdminSpecification(request);
            var page = userAdminRepository.findAll(spec, pageable)
                    .map(mapper::toAdminResponse);
            return mapper.toPageResponse(page);
        } catch (Exception e) {
            throw new BadRequestException("Page request error: " + e.getMessage());
        }
    }

    @Override
    public ApiNoDataResponse lockUser(Long id) {
        UserEntity user = userAdminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Người dùng với ID: " + id));

        user.setStatus(!user.isStatus());
        userAdminRepository.save(user);

        String message = user.isStatus()
                ? "Mở khóa hồ sơ Người dùng  thành công!"
                : "Khóa hồ sơ Người dùng thành công!";

        return ApiNoDataResponse.builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    @Override
    public UserStatusStatisticResponse getUserStatusStatistic() {
        long total = userAdminRepository.count();
        long activeCount = userAdminRepository.countByStatus(true);
        long lockedCount = userAdminRepository.countByStatus(false);

        return UserStatusStatisticResponse.builder()
                .total(total)
                .activeCount(activeCount)
                .lockedCount(lockedCount)
                .build();
    }

}
