package com.pgw.payflow.component.camunda.worker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component("rollbackDelegate")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RollbackWorker implements JavaDelegate {

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    log.info("Rollback executed for transferId={}",
      execution.getVariable("transferId"));

    execution.setVariable("transferStatus", "FAILED");
    execution.setVariable("failReason", "FRAUD_OR_LIMIT_FAILED");

    // TODO: тут будет реальная логика отката, когда понадобится
  }
}