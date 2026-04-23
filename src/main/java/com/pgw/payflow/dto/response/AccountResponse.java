package com.pgw.payflow.dto.response;

import com.pgw.payflow.constant.enums.AccountStatus;
import com.pgw.payflow.constant.enums.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {

    Long id;
    Long balance;
    Currency currency;
    AccountStatus accountStatus;
    Integer dailyLimitUsed;
    Integer dailyLimitMax;
    Long userId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
