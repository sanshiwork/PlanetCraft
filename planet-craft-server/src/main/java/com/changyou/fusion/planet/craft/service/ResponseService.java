package com.changyou.fusion.planet.craft.service;

import com.changyou.fusion.planet.craft.domain.BusinessException;
import com.changyou.fusion.planet.craft.domain.Response;

/**
 * HTTP响应相关服务
 * Created by zhanglei_js on 2017/9/1.
 */
public interface ResponseService {

    /**
     * 响应码
     */
    enum CODE {

        SUCCESS("0000"), // 0000=处理成功
        ILLEGAL_PARAMETER("1000"), // 1000=参数错误:{0}
        UNKNOWN("9999"); // 9999=未知异常:{0}


        CODE(String value) {
            this.value = value;
        }

        // 获取响应码
        public String getValue() {
            return this.value;
        }

        // 响应码值
        private String value;
    }

    public <T> Response<T> success(T data);

    public BusinessException newBusinessException(CODE code, Object... objects);
}
