package com.changyou.fusion.planet.craft.domain;

/**
 * 公共响应
 * Created by zhanglei_js on 2017/9/1.
 */
public class Response<T> {

    private Result result;

    private T data;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "result=" + result +
                ", data=" + data +
                '}';
    }
}
