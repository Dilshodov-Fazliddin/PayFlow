package com.pgw.payflow.service.impl;

import com.pgw.payflow.dto.request.TransferToProcess;
import com.pgw.payflow.service.CamundaStartTransferProcess;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CamundaStartTransferProcessImpl implements CamundaStartTransferProcess {

  RuntimeService runtimeService;

  @Override
  public void startTransfer(TransferToProcess request) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("fromAccount", request.getFromAccount());
    variables.put("toAccount", request.getToAccount());
    variables.put("amount", request.getAmount());
    variables.put("transferId", request.getTransferId());
    variables.put("validationPassed", request.getValidatePassed());


    runtimeService.startProcessInstanceByKey(
      "paymentProcess",
      request.getTransferId().toString(),
      variables);
  }
}
