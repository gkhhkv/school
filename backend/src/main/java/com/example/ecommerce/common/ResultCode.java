package com.example.ecommerce.common;

/**
 * 统一返回码枚举。
 */
public enum ResultCode {

    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "参数错误"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    INSUFFICIENT_STOCK(2001, "库存不足"),
    ORDER_NOT_FOUND(2002, "订单不存在"),
    INVALID_STATUS_TRANSITION(2003, "无效的状态变更"),
    ORDER_CANNOT_CANCEL(2004, "该订单无法取消"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    LOGIN_FAILED(2005, "用户名或密码错误"),
    USERNAME_EXISTS(2006, "用户名已存在");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
