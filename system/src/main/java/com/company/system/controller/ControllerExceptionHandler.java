package com.company.system.controller;

import com.company.server.dto.ResponseDto;
import com.company.server.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    //只要抛出的异常是BusinessException，就会被拦截
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseDto businessExceptionHandler(BusinessException e) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(false);
        log.error("业务异常：{}", e.getCode().getDesc());
        responseDto.setMessage(e.getCode().getDesc());
        return responseDto;
    }

}
