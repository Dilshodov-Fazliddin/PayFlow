package com.pgw.payflow.service;

import com.pgw.payflow.dto.request.AccountCreateRequest;
import com.pgw.payflow.dto.response.AccountResponse;

public interface AccountService {
    AccountResponse createAccount(AccountCreateRequest createRequest);
}
