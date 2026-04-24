package com.pgw.payflow.service;

import com.pgw.payflow.dto.request.AccountCreateRequest;
import com.pgw.payflow.dto.response.AccountResponse;

import javax.security.auth.login.AccountNotFoundException;

public interface AccountService {
    AccountResponse createAccount(AccountCreateRequest createRequest);

    boolean balanceChecker(Long fromAccountId, Long amount) throws AccountNotFoundException;
}
