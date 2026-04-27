package com.pgw.payflow.component.camunda.worker;

import com.pgw.payflow.constant.enums.TransferStatus;
import com.pgw.payflow.exception.TransferCanceledException;
import com.pgw.payflow.repository.TransferRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.pgw.payflow.component.camunda.valueObject.Constant.AMOUNT;
import static com.pgw.payflow.component.camunda.valueObject.Constant.FROM_ACCOUNT;
import static com.pgw.payflow.constant.enums.TransferStatus.COMPLETED;

@Slf4j
@Component("checkTransferFrequencyDelegate")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CheckTransferFrequencyWorker implements JavaDelegate {

  TransferRepository transferRepository;

  private static final int FREQUENCY_THRESHOLD = 5;
  private static final BigDecimal REVIEW_THRESHOLD = new BigDecimal("100000");
  private static final BigDecimal REJECT_THRESHOLD = new BigDecimal("1000000");

  @Override
  public void execute(DelegateExecution execution) {

    Long fromAccountId = (Long) execution.getVariable(FROM_ACCOUNT);
    Long amountRaw = (Long) execution.getVariable(AMOUNT);
    log.info("fromAccountId: {}, amountRaw: {}", fromAccountId, amountRaw);
    BigDecimal amount = BigDecimal.valueOf(amountRaw);

    LocalDateTime since = LocalDateTime.now().minusHours(1);
    long recentCount = transferRepository.countByAccountSince(fromAccountId, since);

    boolean frequencySuspicious = recentCount > FREQUENCY_THRESHOLD;

    log.info("Fraud check: accountId={}, recentCount={}, amount={}",
      fromAccountId, recentCount, amount);

    String decision;
    String reason;
    try {
      if (amount.compareTo(REJECT_THRESHOLD) > 0) {
        decision = "REJECTED";
        reason = "Amount exceeds hard reject threshold (" + REJECT_THRESHOLD + ")";
      } else if (frequencySuspicious && amount.compareTo(REVIEW_THRESHOLD) > 0) {
        decision = "REJECTED";
        reason = "Suspicious frequency combined with large amount";
      } else if (amount.compareTo(REVIEW_THRESHOLD) > 0) {
        decision = "REVIEW_REQUIRED";
        reason = "Amount exceeds review threshold (" + REVIEW_THRESHOLD + ")";
      } else if (frequencySuspicious) {
        decision = "REVIEW_REQUIRED";
        reason = "Suspicious transfer frequency: " + recentCount + " in last hour";
      } else {
        decision = "APPROVED";
        reason = "No fraud signals detected";
      }

      log.info("Fraud decision: {} ({})", decision, reason);
      boolean fraudCheckPassed = "APPROVED".equals(decision);
      execution.setVariable("fraudDecision", decision);
      execution.setVariable("fraudReason", reason);
      execution.setVariable("recentTransferCount", recentCount);
      execution.setVariable("allChecksPassed", true);
      execution.setVariable("fraudCheckPassed", fraudCheckPassed);
    }catch (Exception e){
      execution.setVariable("allChecksPassed", false);
      execution.setVariable("transferStatus", "FAILED");
      execution.setVariable("failReason", "Something is wrong");
      throw new TransferCanceledException("Transfer canceled");
    }
  }
}
