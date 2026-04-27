package com.pgw.payflow.service.impl;

import com.pgw.payflow.constant.enums.TransferStatus;
import com.pgw.payflow.dto.request.TransferCreateRequest;
import com.pgw.payflow.dto.response.TransferResponse;
import com.pgw.payflow.entity.AccountEntity;
import com.pgw.payflow.entity.TransferEntity;
import com.pgw.payflow.entity.UserEntity;
import com.pgw.payflow.exception.DataNotFoundException;
import com.pgw.payflow.mapper.TransferMapper;
import com.pgw.payflow.repository.AccountRepository;
import com.pgw.payflow.repository.TransferRepository;
import com.pgw.payflow.repository.UserRepository;
import com.pgw.payflow.service.CamundaStartTransferProcess;
import com.pgw.payflow.service.TransferService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.pgw.payflow.constant.enums.TransferStatus.COMPLETED;
import static com.pgw.payflow.constant.enums.TransferStatus.FAILED;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferServiceImpl implements TransferService {

  TransferRepository transferRepository;
  AccountRepository accountRepository;
  UserRepository userRepository;
  TransferMapper transferMapper;
  CamundaStartTransferProcess camundaStartTransferProcess;

  @Override
  @Transactional
  public TransferResponse transferCreate(TransferCreateRequest transferCreateRequest) {
    TransferEntity transfer = transferMapper.toEntity(transferCreateRequest);
    transfer = transferRepository.save(transfer);

    UserEntity fromUser = userRepository.findById(transferCreateRequest.getFromUserId()).orElseThrow(() -> new DataNotFoundException("From User not found"));
    UserEntity toUser = userRepository.findById(transferCreateRequest.getToUserId()).orElseThrow(() -> new DataNotFoundException("To User not found"));

    transfer.setFromAccount(fromUser);
    transfer.setToAccount(toUser);

    transferRepository.save(transfer);
    camundaStartTransferProcess.startTransfer(transferMapper.toProcess(transfer));

    TransferEntity transferToResponse = transferRepository.findById(transfer.getId()).orElseThrow(() -> new DataNotFoundException("Transfer not found"));
    return transferMapper.toResponse(transferToResponse);
  }

  @Override
  public void markAsFailed(Long transferId) {
    TransferEntity transfer = transferRepository.findById(transferId)
      .orElseThrow(() -> new DataNotFoundException("Transfer not found"));


    transfer.setCompletedAt(LocalDateTime.now());
    transfer.setTransferStatus(FAILED);
    transferRepository.save(transfer);
  }

  @Override
  public void debitAndCredit(Long transferId,String processInstanceId) {
    TransferEntity transfer = transferRepository
      .findById(transferId)
      .orElseThrow(()-> new DataNotFoundException("Transfer not found"));

    AccountEntity fromAccount = accountRepository
      .findById(transfer.getFromAccount().getId())
      .orElseThrow(() -> new DataNotFoundException("Account not found wit id: " + transfer.getFromAccount().getId()));

    AccountEntity toAccount = accountRepository
      .findById(transfer.getToAccount().getId())
      .orElseThrow(() -> new DataNotFoundException("Account not found wit id: " + transfer.getToAccount().getId()));


    Long amount = transfer.getAmount();

    fromAccount.setBalance(fromAccount.getBalance() - amount);
    toAccount.setBalance(toAccount.getBalance() + amount);
    transfer.setProcessInstanceId(processInstanceId);
    transfer.setTransferStatus(COMPLETED);
    transfer.setCompletedAt(LocalDateTime.now());
  }

}
