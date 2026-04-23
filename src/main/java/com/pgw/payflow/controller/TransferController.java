package com.pgw.payflow.controller;

import com.pgw.payflow.dto.request.TransferCreateRequest;
import com.pgw.payflow.dto.response.TransferResponse;
import com.pgw.payflow.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

  private final TransferService transferService;

  @PostMapping
  public ResponseEntity<TransferResponse> createTransfer(
    @RequestBody TransferCreateRequest request) {
    TransferResponse response = transferService.transferCreate(request);
    return ResponseEntity.ok(response);
  }
}