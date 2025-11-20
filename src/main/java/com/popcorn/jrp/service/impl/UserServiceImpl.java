package com.popcorn.jrp.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.popcorn.jrp.domain.dto.user.UserDto;
import com.popcorn.jrp.domain.entity.UserEntity;
import com.popcorn.jrp.domain.enums.Role;
import com.popcorn.jrp.repository.UserRepository;
import com.popcorn.jrp.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsersWithIsCandidateForKafka() {
        try {
            List<UserDto> users = userRepository.findByRole(Role.candidate).stream()
                    .map(user -> UserDto.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .role(user.getRole())
                            .build())
                    .collect(Collectors.toList());
            return users;
        } catch (Exception e) {
            log.error("Error in getAllUsersWithIsCandidateForKafka: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public UserDto getUserDetailForKafka(Long userId) {
        try {
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                UserDto userDto = UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build();
                return userDto;
            }
            return null;
        } catch (Exception e) {
            log.error("Error in getUserDetailForKafka: {}", e.getMessage());
            return null;
        }
    }
}
