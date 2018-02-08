package com.changyou.fusion.planet.craft.service.impl;

import com.changyou.fusion.planet.craft.service.LocaleMessageSourceService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 国际化相关服务实现
 * Created by zhanglei_js on 2017/9/4.
 */
@Service
public class LocaleMessageSourceServiceImpl implements LocaleMessageSourceService {

    @Resource
    private MessageSource messageSource;


    @Override
    public String getMessage(String code, Object... objects) {
        return messageSource.getMessage(code, objects, LocaleContextHolder.getLocale());
    }
}
