package com.pgw.payflow.service.impl;

import com.pgw.payflow.dto.request.TransferToProcess;
import com.pgw.payflow.service.CamundaStartTransferProcess;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pgw.payflow.component.camunda.valueObject.Constant.AMOUNT;
import static com.pgw.payflow.component.camunda.valueObject.Constant.FROM_ACCOUNT;
import static com.pgw.payflow.component.camunda.valueObject.Constant.TO_ACCOUNT;
import static com.pgw.payflow.component.camunda.valueObject.Constant.TRANSFER_ID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CamundaStartTransferProcessImpl implements CamundaStartTransferProcess {

  RuntimeService runtimeService;
  HistoryService historyService;

  @Override
  public void startTransfer(TransferToProcess request) {
    final Map<String, Object> variables = new HashMap<>();
    variables.put(FROM_ACCOUNT, request.getFromAccount());
    variables.put(TO_ACCOUNT, request.getToAccount());
    variables.put(AMOUNT, request.getAmount());
    variables.put(TRANSFER_ID, request.getTransferId());
    variables.put("validationPassed", request.getValidatePassed());

    ProcessInstance instance = runtimeService.startProcessInstanceByKey(
      "paymentProcess",
      variables);

    String processInstanceId = instance.getProcessInstanceId();

    List<HistoricVariableInstance> historicVars = historyService
      .createHistoricVariableInstanceQuery()
      .processInstanceId(processInstanceId)
      .list();

    String status = historicVars.stream()
      .filter(v -> "transferStatus".equals(v.getName()))
      .map(v -> (String) v.getValue())
      .findFirst()
      .orElse("UNKNOWN");

    if (!"COMPLETED".equals(status)) {
      historicVars.forEach(v ->
        System.out.println("VAR: " + v.getName() + " = " + v.getValue())
      );
      String reason = historicVars.stream()
        .filter(v -> "failReason".equals(v.getName()))
        .map(v -> (String) v.getValue())
        .findFirst()
        .orElse("UNKNOWN_REASON");
      throw new RuntimeException(reason);
    }
  }
}