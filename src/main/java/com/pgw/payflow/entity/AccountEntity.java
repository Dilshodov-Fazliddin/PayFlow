package com.pgw.payflow.entity;

import com.pgw.payflow.constant.AccountStatus;
import com.pgw.payflow.constant.Currency;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountEntity extends BaseEntity{
    @Column(nullable = false)
    Long balance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Currency currency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AccountStatus accountStatus;

    @Column(nullable = false)
    Integer dailyLimitUsed;

    @Column(nullable = false)
    Integer dailyLimitMax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;
}