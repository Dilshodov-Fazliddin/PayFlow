package com.pgw.payflow.controller;

import com.pgw.payflow.dto.request.AccountCreateRequest;
import com.pgw.payflow.dto.response.AccountResponse;
import com.pgw.payflow.service.AccountService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AccountController {

    AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse>createAccount(@RequestBody @Valid AccountCreateRequest accountCreateRequest){
        return ResponseEntity.ok(accountService.createAccount(accountCreateRequest));
    }
}
