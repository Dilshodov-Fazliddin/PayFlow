package com.pgw.payflow.exception;

import com.pgw.payflow.constant.error.ErrorType;
import org.springframework.http.HttpStatus;

import static com.pgw.payflow.constant.error.Error.DATA_NOT_FOUND_ERROR_CODE;

public class DataNotFoundException extends ApplicationException {

    public DataNotFoundException(String message) {
        super(DATA_NOT_FOUND_ERROR_CODE.getCode(), message, ErrorType.INTERNAL, HttpStatus.NOT_FOUND);
    }
}
