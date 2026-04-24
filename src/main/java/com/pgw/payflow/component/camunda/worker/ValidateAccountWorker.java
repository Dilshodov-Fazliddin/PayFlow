package com.pgw.payflow.component.camunda.worker;

import com.pgw.payflow.component.camunda.valueObject.Constant;
import com.pgw.payflow.constant.enums.AccountStatus;
import com.pgw.payflow.exception.DataNotFoundException;
import com.pgw.payflow.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static com.pgw.payflow.component.camunda.valueObject.Constant.*;

@Slf4j
@Component("validateAccountsDelegate")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ValidateAccountWorker implements JavaDelegate {

  AccountRepository accountRepository;

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    Long transferId = (Long) execution.getVariable(TRANSFER_ID);
    Long fromAccountId = (Long) execution.getVariable(FROM_ACCOUNT);
    Long toAccountId = (Long) execution.getVariable(TO_ACCOUNT);

    log.info("Validating accounts for transferId={}, from={}, to={}, variable = {}",
      transferId, fromAccountId, toAccountId,execution.getVariables());


     accountRepository.findByIdAndAccountStatus(fromAccountId, AccountStatus.ACTIVE).orElseThrow(() -> new DataNotFoundException("Account can not transfer with id: " + fromAccountId));

     accountRepository.findByIdAndAccountStatus(toAccountId,AccountStatus.ACTIVE).orElseThrow(() -> new DataNotFoundException("Account can not transfer with id: " + toAccountId));


    execution.setVariable("validationPassed", true);
    execution.setVariable("accountValid", true);
  }

  private BpmnError fail(String reason) {
    return new BpmnError("TRANSFER_FAILED", reason);
  }
}
