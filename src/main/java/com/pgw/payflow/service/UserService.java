package com.pgw.payflow.service;

import com.pgw.payflow.entity.UserEntity;

public interface UserService {

    UserEntity findByUserId(Long userId);

}
