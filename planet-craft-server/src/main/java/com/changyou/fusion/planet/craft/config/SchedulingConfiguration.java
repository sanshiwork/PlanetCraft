package com.changyou.fusion.planet.craft.config;

import com.changyou.fusion.planet.craft.service.TickService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Spring boot 定时任务配置
 * <p>
 * Created by zhanglei_js on 2017/9/15.
 */
@Configuration
@EnableScheduling
public class SchedulingConfiguration {

    private static Logger logger = LoggerFactory.getLogger(SchedulingConfiguration.class);

    @Autowired
    private TickService tickService;

    /**
     * 5分钟一次
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void scheduler() {
        logger.info("DB Save start.");
        tickService.save();
    }
}
