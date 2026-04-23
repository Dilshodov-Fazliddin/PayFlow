package com.pgw.payflow.component.camunda.worker;

import com.pgw.payflow.repository.TransferRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component("checkDailyLimitDelegate")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CheckDailyLimitWorker implements JavaDelegate {

  TransferRepository transferRepository;

  @Override
  public void execute(DelegateExecution execution) {

    Long fromAccount = (Long) execution.getVariable("fromAccount");
    Long amount = (Long) execution.getVariable("amount");
    Integer dailyLimit = (Integer) execution.getVariable("dailyLimitMax");

    LocalDate today = LocalDate.now();
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

    BigDecimal spentToday = transferRepository
      .sumSuccessfulTransfersByAccountAndDate(fromAccount, startOfDay, endOfDay);

    if (spentToday == null) {
      spentToday = BigDecimal.ZERO;
    }

    BigDecimal currentAmount = BigDecimal.valueOf(amount);
    BigDecimal limit = BigDecimal.valueOf(dailyLimit);

    BigDecimal projected = spentToday.add(currentAmount);

    boolean limitExceeded = projected.compareTo(limit) > 0;

    log.info("Daily limit check: account={}, spentToday={}, currentAmount={}, projected={}, limit={}, exceeded={}",
      fromAccount, spentToday, currentAmount, projected, limit, limitExceeded
    );

    execution.setVariable("limitExceeded", limitExceeded);
    execution.setVariable("allChecksPassed", true);
    execution.setVariable("limitOk", true);
  }
}