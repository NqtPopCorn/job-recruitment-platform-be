package com.popcorn.jrp.service;

import java.util.List;

import com.popcorn.jrp.domain.dto.user.UserDto;

public interface UserService {
    public List<UserDto> getAllUsersWithIsCandidateForKafka();

    public UserDto getUserDetailForKafka(Long userId);
}
