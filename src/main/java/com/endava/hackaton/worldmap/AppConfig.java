package com.endava.hackaton.worldmap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan({"com.endava.hackaton.worldmap"})
public class AppConfig {

    @Bean
    public ExecutorService getExecutionService(){
        return Executors.newFixedThreadPool(10);
    }

}
