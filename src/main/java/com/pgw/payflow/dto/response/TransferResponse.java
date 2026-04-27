package com.pgw.payflow.dto.response;

import com.pgw.payflow.constant.enums.Currency;
import com.pgw.payflow.constant.enums.TransferStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferResponse {

    Long id;
    Long fromUserId;
    Long toUserId;
    Long amount;
    Currency currency;
    TransferStatus transferStatus;
    String processInstanceId;
    String failureReason;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime completedAt;
}
