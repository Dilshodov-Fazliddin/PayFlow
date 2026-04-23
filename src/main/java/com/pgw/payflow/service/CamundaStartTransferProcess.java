package com.pgw.payflow.service;

import com.pgw.payflow.dto.request.TransferToProcess;

public interface CamundaStartTransferProcess {
  void startTransfer(TransferToProcess transferToProcess);
}
