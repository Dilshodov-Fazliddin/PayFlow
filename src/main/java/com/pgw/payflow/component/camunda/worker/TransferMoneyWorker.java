package com.pgw.payflow.component.camunda.worker;

import com.pgw.payflow.service.TransferService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component("transferMoneyDelegate")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class TransferMoneyWorker implements JavaDelegate {

  TransferService transferService;

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    Long fromAccountId = (Long) execution.getVariable("fromAccount");
    Long toAccountId = (Long) execution.getVariable("toAccount");
    Long amount = (Long) execution.getVariable("amount");
    Long transactionId = (Long) execution.getVariable("transferId");

    log.info("Executing transfer {}: {} -> {}, amount={}",
      transactionId, fromAccountId, toAccountId, amount);


    try {
      transferService.debitAndCredit(transactionId);
    }catch (Exception e){
      log.error("Transfer execution failed for transferId={}", transactionId, e);
      throw new BpmnError("TRANSFER_FAILED", "Execution failed: " + e.getMessage());
    }
  }
}
