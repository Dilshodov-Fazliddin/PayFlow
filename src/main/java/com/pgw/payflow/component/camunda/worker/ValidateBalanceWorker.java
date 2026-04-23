package com.pgw.payflow.component.camunda.worker;

import com.pgw.payflow.entity.AccountEntity;
import com.pgw.payflow.exception.TransferCanceledException;
import com.pgw.payflow.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component("validateBalanceDelegate")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ValidateBalanceWorker implements JavaDelegate {
  AccountRepository accountRepository;

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    Long fromAccountId = (Long) execution.getVariable("fromAccount");
    Long amount = (Long) execution.getVariable("amount");

    AccountEntity fromAccount = accountRepository.findById(fromAccountId).orElseThrow(()-> new BpmnError("TRANSFER FAILED","FAIL"));

    log.info("Checking balance: accountId={}, balance={}, required={}",
      fromAccountId, fromAccount.getBalance(), amount);

    if (fromAccount.getBalance() < amount) {
      throw new BpmnError("TRANSFER_FAILED",
        "Insufficient funds: balance=" + fromAccount.getBalance() + ", required=" + amount);
    }
    execution.setVariable("balanceValid", true);
  }
}
