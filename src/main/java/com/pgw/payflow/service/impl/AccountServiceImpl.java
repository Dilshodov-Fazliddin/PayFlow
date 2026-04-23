package com.pgw.payflow.service.impl;

import com.pgw.payflow.constant.AccountStatus;
import com.pgw.payflow.dto.request.AccountCreateRequest;
import com.pgw.payflow.dto.response.AccountResponse;
import com.pgw.payflow.entity.AccountEntity;
import com.pgw.payflow.entity.UserEntity;
import com.pgw.payflow.mapper.AccountMapper;
import com.pgw.payflow.repository.AccountRepository;
import com.pgw.payflow.service.AccountService;
import com.pgw.payflow.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    AccountMapper accountMapper;
    UserService userService;


    @Override
    public AccountResponse createAccount(AccountCreateRequest createRequest) {
        UserEntity user = userService.findByUserId(createRequest.getUserId());
        AccountEntity accountEntity = accountMapper.toEntity(createRequest);
        accountEntity.setAccountStatus(AccountStatus.ACTIVE);
        accountEntity.setUser(user);
        return accountMapper.toResponse(accountRepository.save(accountEntity));
    }
}
