package com.changyou.fusion.planet.craft.domain;

/**
 * 响应结果
 * Created by zhanglei_js on 2017/9/1.
 */
public class Result {

    /**
     * 返回码
     */
    private String code;

    /**
     * 消息
     */
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
