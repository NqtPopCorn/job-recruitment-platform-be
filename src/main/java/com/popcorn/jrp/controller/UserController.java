package com.popcorn.jrp.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.popcorn.jrp.domain.dto.user.UserDto;
import com.popcorn.jrp.domain.request.user.UserQueryAdmin;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.ApiResultsResponse;
import com.popcorn.jrp.domain.response.user.UserAdminResponse;
import com.popcorn.jrp.service.UserService;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Dùng cho webflux lấy giá trị candidate từ main service
    @GetMapping("/candidates/web-client")
    public ResponseEntity<List<UserDto>> getUsersIsCandidateForKafka() {
        List<UserDto> userDtos = userService.getAllUsersWithIsCandidateForKafka();
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/detail/{userId}/web-client")
    public ResponseEntity<UserDto> getUserDetailByUserIdForKafka(@PathVariable("userId") Long userId) {
        UserDto userDto = userService.getUserDetailForKafka(userId);
        return ResponseEntity.ok(userDto);
    }
}
