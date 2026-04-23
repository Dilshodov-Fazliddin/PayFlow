package com.pgw.payflow.dto.request;

import com.pgw.payflow.constant.enums.Currency;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountCreateRequest {

    @NotNull(message = "Currency is required")
    Currency currency;

    @NotNull(message = "Daily limit is required")
    @Min(value = 1, message = "Daily limit must be at least 1")
    Integer dailyLimitMax;

    @NotNull(message = "User ID is required")
    Long userId;
}
