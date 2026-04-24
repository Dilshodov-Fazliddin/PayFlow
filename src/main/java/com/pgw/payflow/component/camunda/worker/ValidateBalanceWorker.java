package com.pgw.payflow.component.camunda.worker;

import com.pgw.payflow.entity.AccountEntity;
import com.pgw.payflow.exception.TransferCanceledException;
import com.pgw.payflow.repository.AccountRepository;
import com.pgw.payflow.repository.TransferRepository;
import com.pgw.payflow.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static com.pgw.payflow.component.camunda.valueObject.Constant.AMOUNT;
import static com.pgw.payflow.component.camunda.valueObject.Constant.FROM_ACCOUNT;

@Slf4j
@Component("validateBalanceDelegate")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ValidateBalanceWorker implements JavaDelegate {
  AccountRepository accountRepository;
  AccountService accountService;

  @Override
  public void execute(DelegateExecution execution) throws Exception {

    Long fromAccountId = (Long) execution.getVariable(FROM_ACCOUNT);
    Long amount = (Long) execution.getVariable(AMOUNT);

    AccountEntity fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() -> new BpmnError("TRANSFER FAILED", "FAIL"));

    log.info("Checking balance: accountId={}, balance={}, required={}",
      fromAccountId, fromAccount.getBalance(), amount);

    boolean checker = accountService.balanceChecker(fromAccountId, amount);

    if (!checker){
      throw new BpmnError("TRANSFER FAILED", "FAIL");
  }
    execution.setVariable("balanceValid", true);
  }
}
