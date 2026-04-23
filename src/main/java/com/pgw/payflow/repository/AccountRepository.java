package com.pgw.payflow.repository;

import com.pgw.payflow.constant.enums.AccountStatus;
import com.pgw.payflow.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity,Long> {
  Optional<AccountEntity> findByIdAndAccountStatus(Long id, AccountStatus accountStatus);

}
