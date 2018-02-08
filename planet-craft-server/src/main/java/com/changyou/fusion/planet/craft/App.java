package com.changyou.fusion.planet.craft;

import com.changyou.fusion.planet.craft.service.TickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class App implements CommandLineRunner {

    private final TickService tickService;

    @Autowired
    public App(TickService tickService) {
        this.tickService = tickService;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        // Executors.newScheduledThreadPool(1).scheduleAtFixedRate(tickService::tick, 0, 10, TimeUnit.MILLISECONDS);
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(tickService::tick, 0, 10, TimeUnit.MILLISECONDS);
    }
}
