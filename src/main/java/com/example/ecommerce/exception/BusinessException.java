package com.example.ecommerce.exception;

import com.example.ecommerce.common.ResultCode;

/**
 * 业务异常。
 */
public class BusinessException extends RuntimeException {

    private final int code;

    /**
     * @param resultCode 错误码
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * @param code    错误码
     * @param message 错误消息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
