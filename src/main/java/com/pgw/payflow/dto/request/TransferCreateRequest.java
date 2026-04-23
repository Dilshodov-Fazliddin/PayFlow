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
public class TransferCreateRequest {

    @NotNull(message = "Sender user ID is required")
    Long fromUserId;

    @NotNull(message = "Receiver user ID is required")
    Long toUserId;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be at least 1")
    Long amount;

    @NotNull(message = "Currency is required")
    Currency currency;
}
