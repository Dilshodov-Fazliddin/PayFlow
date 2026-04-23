package com.pgw.payflow.service.impl;

import com.pgw.payflow.entity.UserEntity;
import com.pgw.payflow.exception.DataNotFoundException;
import com.pgw.payflow.repository.UserRepository;
import com.pgw.payflow.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public UserEntity findByUserId(Long userId) {
        return userRepository
                .findById(userId).orElseThrow(()-> new DataNotFoundException("User not found with id:" + userId));
    }
}
