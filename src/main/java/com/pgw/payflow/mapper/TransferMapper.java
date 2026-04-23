package com.pgw.payflow.mapper;

import com.pgw.payflow.constant.TransferStatus;
import com.pgw.payflow.dto.request.TransferCreateRequest;
import com.pgw.payflow.dto.response.TransferResponse;
import com.pgw.payflow.entity.TransferEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", imports = {TransferStatus.class})
public interface TransferMapper {

    @Mapping(source = "fromAccount.id", target = "fromUserId")
    @Mapping(source = "toAccount.id", target = "toUserId")
    TransferResponse toResponse(TransferEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "fromAccount", ignore = true)
    @Mapping(target = "toAccount", ignore = true)
    @Mapping(target = "transferStatus", expression = "java(TransferStatus.WAITING)")
    @Mapping(target = "processInstanceId", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "failureReason", ignore = true)
    TransferEntity toEntity(TransferCreateRequest request);
}
