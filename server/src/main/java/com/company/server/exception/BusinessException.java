package com.company.server.exception;

public class BusinessException extends RuntimeException {

    private BusinessExceptionCode code;

    public BusinessException(BusinessExceptionCode code) {
        super(code.getDesc());
        this.code = code;
    }

    public BusinessExceptionCode getCode() {
        return code;
    }

    public void setCode(BusinessExceptionCode code) {
        this.code = code;
    }

    /**
     * 不写入堆栈信息，提高性能
     * <p>
     * 抛出业务异常时，不打印堆栈信息，
     * 一方面是提高性能，
     * 另一方面是没有业务异常没必要看堆栈信息
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}