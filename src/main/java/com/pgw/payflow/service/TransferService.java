package com.pgw.payflow.service;

import com.pgw.payflow.dto.request.TransferCreateRequest;
import com.pgw.payflow.dto.response.TransferResponse;

public interface TransferService {
  TransferResponse transferCreate(TransferCreateRequest transferCreateRequest);

  void markAsFailed(Long transferId);

  void debitAndCredit(Long transferId,String processInstanceId);
}
