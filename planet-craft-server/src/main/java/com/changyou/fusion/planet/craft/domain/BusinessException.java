package com.changyou.fusion.planet.craft.domain;

/**
 * 自定义异常
 * Created by zhanglei_js on 2017/9/4.
 */
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private String code;

    /**
     * 错误消息
     */
    private String message;

    public BusinessException(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
