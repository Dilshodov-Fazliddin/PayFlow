package com.pgw.payflow.repository;

import com.pgw.payflow.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<TransferEntity,Long> {
  @Query("""
    SELECT COALESCE(SUM(t.amount), 0)
    FROM TransferEntity t
    WHERE t.fromAccount.id = :accountId
      AND t.transferStatus = 'SUCCESS'
      AND t.createdAt >= :startOfDay
      AND t.createdAt < :endOfDay
""")
  BigDecimal sumSuccessfulTransfersByAccountAndDate(
    @Param("accountId") Long accountId,
    @Param("startOfDay") LocalDateTime startOfDay,
    @Param("endOfDay") LocalDateTime endOfDay
  );


  @Query("""
           SELECT COUNT(t)
           FROM TransferEntity t
           WHERE t.fromAccount.id = :fromAccountId
             AND t.createdAt >= :since
           """)
  long countByAccountSince(@Param("fromAccountId") Long fromAccountId,
                           @Param("since") LocalDateTime since);
}
