package com.pgw.payflow.entity;

import com.pgw.payflow.constant.Currency;
import com.pgw.payflow.constant.TransferStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferEntity extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity fromAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity toAccount;
    Long amount;
    Currency currency;
    @Enumerated(EnumType.STRING)
    TransferStatus transferStatus;
    String processInstanceId;
    LocalDateTime completedAt;
    String failureReason;
}
