package com.changyou.fusion.planet.craft.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring boot MVC 全局处理
 * <p>
 * Created by zhanglei_js on 2017/9/1.
 */
@Configuration
public class ServerMvcConfiguration extends WebMvcConfigurerAdapter {

    /**
     * 跨域处理
     *
     * @param registry 跨域配置对象
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true);
    }

}
