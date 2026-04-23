package com.pgw.payflow.dto.request;

import com.pgw.payflow.constant.AccountStatus;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountUpdateRequest {

    @Min(value = 1, message = "Daily limit must be at least 1")
    Integer dailyLimitMax;

    AccountStatus accountStatus;
}
