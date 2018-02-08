package com.changyou.fusion.planet.craft.service.impl;

import com.changyou.fusion.planet.craft.domain.BusinessException;
import com.changyou.fusion.planet.craft.domain.Response;
import com.changyou.fusion.planet.craft.domain.Result;
import com.changyou.fusion.planet.craft.service.LocaleMessageSourceService;
import com.changyou.fusion.planet.craft.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * HTTP响应相关服务实现
 * <p>
 * Created by zhanglei_js on 2017/9/4.
 */
@Service
public class ResponseServiceImpl implements ResponseService {

    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;

    @Override
    public <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        Result result = new Result();
        result.setCode(CODE.SUCCESS.getValue());
        result.setMessage(localeMessageSourceService.getMessage(CODE.SUCCESS.getValue()));
        response.setResult(result);
        response.setData(data);
        return response;
    }

    @Override
    public BusinessException newBusinessException(CODE code, Object... objects) {
        return new BusinessException(code.getValue(), localeMessageSourceService.getMessage(code.getValue(), objects));
    }
}
