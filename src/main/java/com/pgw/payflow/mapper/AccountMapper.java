package com.pgw.payflow.mapper;

import com.pgw.payflow.constant.enums.AccountStatus;
import com.pgw.payflow.dto.request.AccountCreateRequest;
import com.pgw.payflow.dto.request.AccountUpdateRequest;
import com.pgw.payflow.dto.response.AccountResponse;
import com.pgw.payflow.entity.AccountEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", imports = {AccountStatus.class})
public interface AccountMapper {

    @Mapping(source = "user.id", target = "userId")
    AccountResponse toResponse(AccountEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "balance", expression = "java(0L)")
    @Mapping(target = "dailyLimitUsed", expression = "java(0)")
    @Mapping(target = "accountStatus", expression = "java(AccountStatus.NOT_ACTIVE)")
    AccountEntity toEntity(AccountCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "dailyLimitUsed", ignore = true)
    void updateEntity(AccountUpdateRequest request, @MappingTarget AccountEntity entity);
}
