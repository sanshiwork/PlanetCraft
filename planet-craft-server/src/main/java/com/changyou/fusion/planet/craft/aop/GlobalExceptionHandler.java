package com.changyou.fusion.planet.craft.aop;

import com.changyou.fusion.planet.craft.domain.BusinessException;
import com.changyou.fusion.planet.craft.domain.Response;
import com.changyou.fusion.planet.craft.domain.Result;
import com.changyou.fusion.planet.craft.service.LocaleMessageSourceService;
import com.changyou.fusion.planet.craft.service.ResponseService;
import com.changyou.fusion.planet.craft.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * <p>
 * Created by zhanglei_js on 2017/9/25.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;

    @Autowired
    private SessionService sessionService;

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Response<Object> handleBindException(BindException ex) throws Exception {
        Response<Object> response = new Response<>();
        Result result = new Result();
        result.setCode(ResponseService.CODE.ILLEGAL_PARAMETER.getValue());
        Object field = ex.getFieldError().getField();
        Object msg = ex.getFieldError().getDefaultMessage();
        result.setMessage(localeMessageSourceService.getMessage(ResponseService.CODE.ILLEGAL_PARAMETER.getValue(), field, msg));
        response.setResult(result);

        // 打印日志
        HttpServletRequest request = sessionService.getHttpServletRequest();
        logger.error("参数异常: RemoteHost -> {} & URI -> {} & Response -> {}", sessionService.ip(), request.getRequestURI(), response);

        return response;
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Response<Object> handleBusinessException(BusinessException ex) throws Exception {
        Response<Object> response = new Response<>();
        Result result = new Result();
        result.setCode(ex.getCode());
        result.setMessage(ex.getMessage());
        response.setResult(result);

        // 打印日志
        HttpServletRequest request = sessionService.getHttpServletRequest();
        logger.error("自定义异常: RemoteHost -> {} & URI -> {} & Response -> {}", sessionService.ip(), request.getRequestURI(), response);

        return response;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response<Object> handleException(Exception ex) throws Exception {
        Response<Object> response = new Response<>();
        Result result = new Result();
        result.setCode(ResponseService.CODE.UNKNOWN.getValue());
        result.setMessage(localeMessageSourceService.getMessage(ResponseService.CODE.UNKNOWN.getValue(), ex.getLocalizedMessage()));
        response.setResult(result);
        // 打印日志
        HttpServletRequest request = sessionService.getHttpServletRequest();
        logger.error("未知异常: RemoteHost -> {} & URI -> {} & Response -> {}", sessionService.ip(), request.getRequestURI(), response);
        logger.error("未知异常", ex);
        return response;
    }

}
